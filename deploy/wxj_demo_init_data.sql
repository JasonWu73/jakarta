truncate user;
truncate role;
truncate op_log;

insert into user (id, username, nickname, hashed_password, status, role_id, created_at, updated_at, remark)
values (1, 'wxj', '吴仙杰', '$2a$10$5.HAUrgWzzz6Vkju4X/guOFcVIqPr0c/HFHXBX6QilZTYf38SlK72', 1, 1, '2023-05-09 16:15:42', '2023-05-09 16:15:42', null);

insert into role (id, name, menus, parent_id, parent_name, full_path, created_at, updated_at, remark)
values (1, '超级管理员', 'root', null, null, '1', '2023-05-09 17:24:50', '2023-05-09 17:24:50', null);
