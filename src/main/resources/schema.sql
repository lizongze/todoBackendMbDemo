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
