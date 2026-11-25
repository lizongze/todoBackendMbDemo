-- 因为没有了 JPA 的 ddl-auto，我们需要自己准备建表脚本
-- Spring Boot 会在 mybatis:init:mode=always 时自动执行这个文件

-- todo 表
CREATE TABLE IF NOT EXISTS todo (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    status VARCHAR(50),
    tags TEXT, -- 假设这是一个用逗号分隔的字符串
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 补齐已有 todo 表中的待办字段
ALTER TABLE todo
    ADD COLUMN IF NOT EXISTS planned_finish_time TIMESTAMP,
    ADD COLUMN IF NOT EXISTS completed BOOLEAN,
    ADD COLUMN IF NOT EXISTS description TEXT,
    ADD COLUMN IF NOT EXISTS reminders TEXT[];

-- todo_mark 表
CREATE TABLE IF NOT EXISTS todo_mark (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    color VARCHAR(50)
);

-- todo_mark_relation 表
CREATE TABLE IF NOT EXISTS todo_mark_relation (
    id BIGSERIAL PRIMARY KEY,
    todo_id BIGINT NOT NULL,
    mark_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL
);
