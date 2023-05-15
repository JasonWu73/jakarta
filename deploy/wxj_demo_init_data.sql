truncate user;
truncate role;
truncate op_log;

insert into user (id, username, nickname, hashed_password, status, role_id, created_at, updated_at, remark)
values (1, 'wxj', '吴仙杰', '$2a$10$5.HAUrgWzzz6Vkju4X/guOFcVIqPr0c/HFHXBX6QilZTYf38SlK72', 1, 1, now(), now(), null);

insert into role (id, name, authorities, parent_id, parent_name, full_path, created_at, updated_at, remark)
values (1, '超级管理员', 'root', null, null, '1', now(), now(), null);
insert into role (id, name, authorities, parent_id, parent_name, full_path, created_at, updated_at, remark)
values (2, '管理员', 'root', 1, '超级管理员', '1.2', now(), now(), null);
