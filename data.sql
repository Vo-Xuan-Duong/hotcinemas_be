-- =========================
-- Extensions (khuyến nghị)
-- =========================
CREATE EXTENSION IF NOT EXISTS "citext";
CREATE EXTENSION IF NOT EXISTS "pgcrypto"; -- dùng gen_random_bytes() nếu cần token/QR

-- =========================
-- 1) Rạp, phòng, ghế
-- =========================

CREATE TABLE cinema (
                        id          BIGSERIAL PRIMARY KEY,
                        name        TEXT NOT NULL,
                        city        TEXT,
                        address     TEXT,
                        timezone    TEXT NOT NULL DEFAULT 'Asia/Ho_Chi_Minh',
                        is_active   BOOLEAN NOT NULL DEFAULT TRUE,
                        created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE room (
                      id                   BIGSERIAL PRIMARY KEY,
                      cinema_id            BIGINT NOT NULL REFERENCES cinema(id) ON DELETE CASCADE,
                      name                 TEXT NOT NULL,
                      seat_schema_version  INT  NOT NULL DEFAULT 1,
                      is_active            BOOLEAN NOT NULL DEFAULT TRUE,
                      UNIQUE (cinema_id, name)
);
CREATE INDEX ix_room_cinema ON room(cinema_id);

CREATE TABLE seat (
                      id          BIGSERIAL PRIMARY KEY,
                      room_id     BIGINT NOT NULL REFERENCES room(id) ON DELETE CASCADE,
                      row_label   TEXT NOT NULL,
                      seat_number TEXT NOT NULL,
                      seat_type   TEXT NOT NULL CHECK (seat_type IN ('STANDARD','VIP','COUPLE','ACCESSIBLE')),
                      col         INT  NOT NULL,
                      row         INT  NOT NULL,
                      is_active   BOOLEAN NOT NULL DEFAULT TRUE,
                      UNIQUE (room_id, row_label, seat_number)
);
CREATE INDEX ix_seat_room ON seat(room_id);

-- =========================
-- 2) Phim & suất chiếu
-- =========================

CREATE TABLE movie (
                       id               BIGSERIAL PRIMARY KEY,
                       title            TEXT NOT NULL,                 -- Tựa phát hành tại VN
                       original_title   TEXT,                          -- Tựa gốc
                       synopsis         TEXT,                          -- Tóm tắt
                       duration_min     INT  NOT NULL CHECK (duration_min > 0),
                       age_rating       TEXT,                          -- P/C13/C16/C18... hoặc PG-13/R...
                       genres           TEXT[] DEFAULT '{}',           -- Thể loại
                       release_date     DATE,
                       countries        TEXT[] DEFAULT '{}',           -- Quốc gia sản xuất
                       spoken_languages TEXT[] DEFAULT '{}',           -- Ngôn ngữ thoại
                       subtitles        TEXT[] DEFAULT '{}',           -- Phụ đề sẵn có
                       directors        TEXT[] DEFAULT '{}',
                       cast             JSONB,                         -- [{name, role}]
                       production_companies TEXT[] DEFAULT '{}',
                       distributor      TEXT,
                       poster_url       TEXT,
                       backdrop_url     TEXT,
                       trailer_url      TEXT,
                       imdb_id          TEXT,
                       tmdb_id          TEXT,
                       metadata         JSONB,                         -- mở rộng (keywords, ratings ngoài...)
                       created_at       TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE showtime (
                          id            BIGSERIAL PRIMARY KEY,
                          movie_id      BIGINT NOT NULL REFERENCES movie(id) ON DELETE CASCADE,
                          room_id       BIGINT NOT NULL REFERENCES room(id)  ON DELETE CASCADE,
                          start_time    TIMESTAMPTZ NOT NULL,
                          end_time      TIMESTAMPTZ NOT NULL,
                          language      TEXT,                  -- Vietsub/Thuyết minh/Eng...
                          format        TEXT,                  -- 2D/3D/IMAX/4DX...
                          status        TEXT NOT NULL DEFAULT 'SCHEDULED'
                              CHECK (status IN ('SCHEDULED','OPEN','LOCKED','CLOSED','CANCELLED')),
                          UNIQUE (room_id, start_time)
);
CREATE INDEX ix_showtime_movie ON showtime(movie_id);
CREATE INDEX ix_showtime_time  ON showtime(room_id, start_time);

-- Tình trạng ghế + giá vé theo từng suất chiếu
CREATE TABLE showtime_seat (
                               showtime_id  BIGINT NOT NULL REFERENCES showtime(id) ON DELETE CASCADE,
                               seat_id      BIGINT NOT NULL REFERENCES seat(id)     ON DELETE CASCADE,
                               seat_status  TEXT NOT NULL DEFAULT 'AVAILABLE'
                                   CHECK (seat_status IN ('AVAILABLE','HELD','SOLD','BLOCKED')),
                               price_tier   TEXT NOT NULL CHECK (price_tier IN ('STANDARD','VIP','COUPLE','ACCESSIBLE')),
                               price        NUMERIC(12,2) NOT NULL CHECK (price >= 0),
                               currency     TEXT NOT NULL DEFAULT 'VND',
                               PRIMARY KEY (showtime_id, seat_id)
);
CREATE INDEX ix_showtime_seat_status ON showtime_seat(showtime_id, seat_status);

-- =========================
-- 3) Người dùng
-- =========================

CREATE TABLE app_user (
                          id         BIGSERIAL PRIMARY KEY,
                          email      CITEXT UNIQUE,
                          phone      TEXT UNIQUE,
                          full_name  TEXT,
                          is_active  BOOLEAN NOT NULL DEFAULT TRUE,
                          created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- =========================
-- 4) Giữ ghế & đặt vé (không dùng giỏ hàng)
-- =========================

-- Giữ ghế trực tiếp
CREATE TABLE seat_hold (
                           id             BIGSERIAL PRIMARY KEY,
                           user_id        BIGINT REFERENCES app_user(id) ON DELETE SET NULL,
                           showtime_id    BIGINT NOT NULL REFERENCES showtime(id) ON DELETE CASCADE,
                           seat_id        BIGINT NOT NULL REFERENCES seat(id)     ON DELETE CASCADE,
                           held_until     TIMESTAMPTZ NOT NULL,  -- ví dụ: now() + interval '8 minutes'
                           status         TEXT NOT NULL DEFAULT 'HELD'
                               CHECK (status IN ('HELD','RELEASED','CONFIRMED')),
                           price_snapshot NUMERIC(12,2) NOT NULL,
                           price_tier     TEXT NOT NULL,
                           created_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);
-- Ngăn giữ trùng ghế đang còn hiệu lực/đã xác nhận
CREATE UNIQUE INDEX ux_seat_hold_active
    ON seat_hold(showtime_id, seat_id)
    WHERE status IN ('HELD','CONFIRMED');
CREATE INDEX ix_seat_hold_expiry ON seat_hold(showtime_id, held_until);
CREATE INDEX ix_seat_hold_user   ON seat_hold(user_id);

-- Đơn đặt vé
CREATE TABLE booking (
                         id            BIGSERIAL PRIMARY KEY,
                         user_id       BIGINT REFERENCES app_user(id) ON DELETE SET NULL,
                         showtime_id   BIGINT NOT NULL REFERENCES showtime(id) ON DELETE RESTRICT,
                         booking_code  TEXT UNIQUE NOT NULL,    -- hiển thị cho KH
                         status        TEXT NOT NULL
                             CHECK (status IN ('PENDING','CONFIRMED','CANCELLED','EXPIRED','REFUND_REQUESTED','REFUNDED')),
                         total_amount  NUMERIC(12,2) NOT NULL CHECK (total_amount >= 0),
                         currency      TEXT NOT NULL DEFAULT 'VND',
                         promo_id      BIGINT,                  -- sẽ ràng buộc sau khi tạo bảng promo
                         meta          JSONB,
                         created_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX ix_booking_showtime ON booking(showtime_id);
CREATE INDEX ix_booking_user     ON booking(user_id);

-- Vé/ghế trong đơn
CREATE TABLE booking_seat (
                              id          BIGSERIAL PRIMARY KEY,
                              booking_id  BIGINT NOT NULL REFERENCES booking(id) ON DELETE CASCADE,
                              seat_id     BIGINT NOT NULL REFERENCES seat(id)     ON DELETE RESTRICT,
                              price_paid  NUMERIC(12,2) NOT NULL,
                              price_tier  TEXT NOT NULL,
                              qr_code     TEXT,  -- có thể lưu token/URL
                              UNIQUE (booking_id, seat_id)
);

-- =========================
-- 5) Thanh toán & hoàn tiền
-- =========================

CREATE TABLE payment (
                         id               BIGSERIAL PRIMARY KEY,
                         booking_id       BIGINT NOT NULL REFERENCES booking(id) ON DELETE CASCADE,
                         provider         TEXT NOT NULL,          -- MoMo/ZaloPay/Napas...
                         provider_txn_id  TEXT,
                         status           TEXT NOT NULL
                             CHECK (status IN ('INIT','AUTHORIZED','CAPTURED','FAILED','REFUNDED')),
                         amount           NUMERIC(12,2) NOT NULL CHECK (amount >= 0),
                         currency         TEXT NOT NULL DEFAULT 'VND',
                         idempotency_key  TEXT UNIQUE,
                         created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
                         raw_response     JSONB
);
CREATE INDEX ix_payment_booking ON payment(booking_id);

CREATE TABLE refund (
                        id           BIGSERIAL PRIMARY KEY,
                        payment_id   BIGINT NOT NULL REFERENCES payment(id) ON DELETE CASCADE,
                        amount       NUMERIC(12,2) NOT NULL CHECK (amount >= 0),
                        status       TEXT NOT NULL CHECK (status IN ('REQUESTED','PROCESSING','SUCCEEDED','FAILED')),
                        created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
                        raw_response JSONB
);

-- =========================
-- 6) Khuyến mãi
-- =========================

CREATE TABLE promo (
                       id             BIGSERIAL PRIMARY KEY,
                       code           TEXT UNIQUE NOT NULL,
                       name           TEXT NOT NULL,
                       type           TEXT NOT NULL CHECK (type IN ('PERCENT','AMOUNT','BOGO','BUNDLE')),
                       value          NUMERIC(12,2) NOT NULL,      -- % hoặc số tiền
                       max_discount   NUMERIC(12,2),
                       start_at       TIMESTAMPTZ NOT NULL,
                       end_at         TIMESTAMPTZ NOT NULL,
                       conditions     JSONB,                        -- lọc theo rạp/ngày/format/min spend...
                       usage_limit    INT,
                       per_user_limit INT,
                       is_active      BOOLEAN NOT NULL DEFAULT TRUE,
                       created_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);

ALTER TABLE booking
    ADD CONSTRAINT fk_booking_promo
        FOREIGN KEY (promo_id) REFERENCES promo(id);

CREATE TABLE promo_redemption (
                                  id           BIGSERIAL PRIMARY KEY,
                                  promo_id     BIGINT NOT NULL REFERENCES promo(id) ON DELETE CASCADE,
                                  user_id      BIGINT NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
                                  booking_id   BIGINT UNIQUE REFERENCES booking(id) ON DELETE SET NULL,
                                  redeemed_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX ix_redemption_user ON promo_redemption(user_id);
CREATE INDEX ix_redemption_promo ON promo_redemption(promo_id);

-- =========================
-- 7) Ratings & Review
-- =========================

-- Mỗi user có thể chấm 1 lần/1 phim (1..10; có thể dùng bước 0.5)
CREATE TABLE movie_user_rating (
                                   movie_id    BIGINT NOT NULL REFERENCES movie(id) ON DELETE CASCADE,
                                   user_id     BIGINT NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
                                   rating      NUMERIC(3,1) NOT NULL CHECK (rating >= 1.0 AND rating <= 10.0),
                                   created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
                                   updated_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
                                   PRIMARY KEY (movie_id, user_id)
);
CREATE INDEX ix_mur_movie ON movie_user_rating(movie_id);
CREATE INDEX ix_mur_user  ON movie_user_rating(user_id);

-- Review: có thể kèm rating_opt (nếu muốn gộp thao tác chấm điểm)
CREATE TABLE movie_review (
                              id           BIGSERIAL PRIMARY KEY,
                              movie_id     BIGINT NOT NULL REFERENCES movie(id) ON DELETE CASCADE,
                              user_id      BIGINT NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
                              title        TEXT,
                              body         TEXT NOT NULL,
                              rating_opt   NUMERIC(3,1) CHECK (rating_opt IS NULL OR (rating_opt >= 1.0 AND rating_opt <= 10.0)),
                              has_spoiler  BOOLEAN NOT NULL DEFAULT FALSE,
                              status       TEXT NOT NULL DEFAULT 'PUBLISHED'
                                  CHECK (status IN ('PUBLISHED','PENDING','REJECTED')),
                              created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
                              updated_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
                              UNIQUE (movie_id, user_id)  -- mỗi user 1 review/phim (có thể bỏ nếu muốn nhiều review)
);
CREATE INDEX ix_review_movie  ON movie_review(movie_id);
CREATE INDEX ix_review_user   ON movie_review(user_id);
CREATE INDEX ix_review_status ON movie_review(status);

-- Tuỳ chọn: đồng bộ rating khi user nhập/đổi rating_opt trong review
CREATE OR REPLACE FUNCTION trg_sync_rating_from_review()
RETURNS TRIGGER AS $$
BEGIN
  IF NEW.rating_opt IS NOT NULL THEN
    INSERT INTO movie_user_rating(movie_id, user_id, rating, created_at, updated_at)
    VALUES (NEW.movie_id, NEW.user_id, NEW.rating_opt, now(), now())
    ON CONFLICT (movie_id, user_id)
    DO UPDATE SET rating = EXCLUDED.rating,
                updated_at = now();
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER movie_review_sync_rating
    AFTER INSERT OR UPDATE OF rating_opt ON movie_review
    FOR EACH ROW
    EXECUTE FUNCTION trg_sync_rating_from_review();

-- =========================
-- 8) Nhật ký & Sự kiện
-- =========================

CREATE TABLE audit_log (
                           id          BIGSERIAL PRIMARY KEY,
                           actor_type  TEXT NOT NULL,          -- 'SYSTEM'/'USER'
                           actor_id    TEXT,
                           action      TEXT NOT NULL,          -- 'HOLD_SEAT','RELEASE_SEAT','PAYMENT_SUCCESS',...
                           entity_type TEXT NOT NULL,          -- 'SHOWTIME','BOOKING','SEAT','MOVIE','ROOM'...
                           entity_id   TEXT NOT NULL,
                           created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
                           data        JSONB
);
CREATE INDEX ix_audit_created ON audit_log(created_at);

CREATE TABLE outbox_event (
                              id           BIGSERIAL PRIMARY KEY,
                              topic        TEXT NOT NULL,         -- 'ticket.issued','payment.captured',...
                              payload      JSONB NOT NULL,
                              created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
                              processed_at TIMESTAMPTZ
);
CREATE INDEX ix_outbox_unprocessed ON outbox_event(topic) WHERE processed_at IS NULL;
