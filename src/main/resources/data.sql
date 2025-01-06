# system 密碼 system123
INSERT INTO `user_info` (`id`, `name`, `username`, `password`, `address`, `email`, `national_id`, `birthday`, `active_flag`, `refresh_token`) VALUES
(1, '張承翊', 'max123@example.com', '$2a$10$QDWJ4gglMtRorFY/mQ.m8.gMKqz9/u03RNN3VQhaTSyPUH6ddL.l2', '台北市南港區', 'max123@example.com', 'A123456789', '1969-09-01 00:00:00', 'Y', 'eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJBRE1JTiJdLCJncm91cHMiOlsiQURNSU5fR1JPVVAgIl0sImVtYWlsIjoibWF4MTIzQGV4YW1wbGUuY29tIiwiaXNzIjoiU1lTVEVNIiwic3ViIjoibWF4MTIzQGV4YW1wbGUuY29tIiwiaWF0IjoxNzM1MjE5MjM2LCJleHAiOjE3MzU4MjQwMzZ9.qCNHLxl1gZ1Qx_PRFmHnOyx9MX3ERG6jVKgbew-CgLE');
INSERT INTO `user_info` (`id`, `name`, `username`, `password`, `address`, `email`, `national_id`, `birthday`, `active_flag`, `refresh_token`) VALUES
(2, '張耿豪', 'nick123@example.com', '$2a$10$HsSpBpgOug5aKvi38mLNoONF7PqjiYqPwYnX5afNFWqPj/x9/xEsK', '新北市淡水區', 'nick123@example.com', 'T124149396', '1995-09-01 00:00:00', 'Y', 'eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJBRE1JTiJdLCJncm91cHMiOlsiQURNSU5fR1JPVVAgIiwiU0FMRVNfR1JPVVAgIl0sImVtYWlsIjoibmljazEyM0BleGFtcGxlLmNvbSIsImlzcyI6IlNZU1RFTSIsInN1YiI6Im5pY2sxMjNAZXhhbXBsZS5jb20iLCJpYXQiOjE3MzU0MTI3MzMsImV4cCI6MTczNjAxNzUzM30.RHBqTW6-4D8BRiPUIk4F8x7D1Tuivg6KkoZCiVsq9Fw');
INSERT INTO `user_info` (`id`, `name`, `username`, `password`, `address`, `email`, `national_id`, `birthday`, `active_flag`, `refresh_token`) VALUES
(3, '蔣哈克', 'John123@example.com', '$2a$10$ZTCww4Na1qgtRmuWITMwDemCsUfOD6y.RtdFfQmVvOjb1cnMMlF9W', '台北市內湖區', 'john123@example.com', 'A123456788', NULL, 'Y', NULL);
INSERT INTO `user_info` (`id`, `name`, `username`, `password`, `address`, `email`, `national_id`, `birthday`, `active_flag`, `refresh_token`) VALUES
(4, 'Amy', 'Amy123@example.com', '$2a$10$u3u3ykdaCPxsq.HfnZgW8O5ZBGxzUwaZ5q2O6HZcS/7wjZCtaoWlS', '台北市內湖區', 'amy123@example.com', 'A123456787', NULL, 'Y', NULL),
(5, 'Sally', 'Sally123@example.com', '$2a$10$LvdCq1uWENb10b7qV3KxTusH2V5qbdDKtfaYquthcszcaegUBGsa6', '台北市內湖區', 'sally123@example.com', 'A123456787', '1999-07-05 00:00:00', 'Y', NULL),
(6, '林隆奮', 'frank123@example.com', '$2a$10$.sGJxxh7xJsr8XvEMt8rLeQIS9nAahp.EUE5.DCnygPSuuIWI2Uqi', '台北市內湖區', 'frank123@example.com', 'A123456777', '2000-01-25 00:00:00', 'Y', NULL),
(7, '陳志興', 'mark123@example.com', '$2a$10$nLe2JXhi/TlL64yYYi9v6uBCWEbXZbZabjs5/o128ZKdAAgT.G.8W', '台北市大同區', 'mark123@example.com', 'O123456789', '1982-01-08 00:00:00', 'Y', 'eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6W10sImdyb3VwcyI6W10sImVtYWlsIjoibWFyazEyM0BleGFtcGxlLmNvbSIsImlzcyI6IlNZU1RFTSIsInN1YiI6Im1hcmsxMjNAZXhhbXBsZS5jb20iLCJpYXQiOjE3MzU3NTM4MTEsImV4cCI6MTczNjM1ODYxMX0.F9dpwUSuxxnwa4UsxCfGdD4v5sesOLFcuKLOVCZzwfo'),
(11, '廖偉璇', 'joanne123@example.com', '$2a$10$NJicz.bsjE8.TJ3S/lOW1O6rxVTiATJlov6AtnqJMfP7PQbmz4Xfa', '台北市內湖區', 'joanne123@example.com', 'A123456236', '1997-08-06 00:00:00', 'Y', NULL),
(12, '陳宏旻', 'hongming123@example.com', '$2a$10$2Td.pBmGh/yfgVaJ.wTk5.0pZHZNwdT9Bu2GrHMWlg0QCwWLZsawy', '新北市淡水區', 'hongming123@example.com', 'B123456789', '1987-08-10 00:00:00', 'Y', 'eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6W10sImdyb3VwcyI6W10sImVtYWlsIjoiaG9uZ21pbmcxMjNAZXhhbXBsZS5jb20iLCJpc3MiOiJTWVNURU0iLCJzdWIiOiJob25nbWluZzEyM0BleGFtcGxlLmNvbSIsImlhdCI6MTczNTQ5MTI5NiwiZXhwIjoxNzM2MDk2MDk2fQ.sYxSbh12AT5TcTxInNWRAgG2a-_nMA2Q2HNSTM5NYfg');
INSERT INTO `role_info` (`id`, `code`, `name`, `type`, `description`, `active_flag`) VALUES
(1, 'ADMIN', '管理員', 'ADMIN', '管理者，具有管理者權限。', 'Y');
INSERT INTO `role_info` (`id`, `code`, `name`, `type`, `description`, `active_flag`) VALUES
(3, 'INSIDE_SALES', 'Inside Sales', 'SALES', '業務，主要通過電話、電子郵件、視訊會議等方式與客戶聯繫，通常在辦公室內工作，與客戶進行遠程的銷售活動。', 'Y');
INSERT INTO `role_info` (`id`, `code`, `name`, `type`, `description`, `active_flag`) VALUES
(4, 'OUTSIDE_SALES', 'Outside Sales', 'SALES', '業務，透過親自拜訪客戶、參加會議或參加產品展示會等場合，進行面對面的銷售活動。他們經常外出到客戶公司或其他商業場所工作。', 'Y');
INSERT INTO `role_info` (`id`, `code`, `name`, `type`, `description`, `active_flag`) VALUES
(5, 'CSR', 'CSR', 'SALES', 'Customer Service Representative 的縮寫，直接與客戶互動，負責解答疑問、處理客戶問題、提供支持、促進顧客服務的流程。', 'Y');
INSERT INTO `group_info` (`id`, `type`, `code`, `name`, `description`, `active_flag`) VALUES
(1, 'ADMIN', 'ADMIN_GROUP ', '管理者群組', '管理者群組', 'Y');
INSERT INTO `group_info` (`id`, `type`, `code`, `name`, `description`, `active_flag`) VALUES
(2, 'SALES', 'SALES_GROUP ', '業務群組', '業務群組', 'Y');
INSERT INTO `group_info` (`id`, `type`, `code`, `name`, `description`, `active_flag`) VALUES
(3, 'GUEST', 'GUEST_GROUP', '訪客群組', '訪客所在的群組', 'Y');
INSERT INTO `function_info` (`id`, `type`, `code`, `name`, `action_type`, `description`, `active_flag`) VALUES
(1, 'DASHBOARD', 'VIEW_DASHBOARD', 'ViewDashboard', 'B', '查詢 DashBoard 資料', 'Y');
INSERT INTO `function_info` (`id`, `type`, `code`, `name`, `action_type`, `description`, `active_flag`) VALUES
(3, 'USER', 'QUERY_USER_DATA', 'QueryUserData', 'B', '查詢使用者資料', 'Y');
INSERT INTO `function_info` (`id`, `type`, `code`, `name`, `action_type`, `description`, `active_flag`) VALUES
(4, 'ROLE', 'QUERY_ROLE_DATA', 'QueryRoleData', 'B', '查詢角色資料', 'Y');
INSERT INTO `function_info` (`id`, `type`, `code`, `name`, `action_type`, `description`, `active_flag`) VALUES
(5, 'USER', 'ADD_USER_DATA', 'AddUserData', 'A', '新增使用者資料', 'Y'),
(6, 'USER', 'DELETE_USER_DATA', 'DeleteUserData', 'D', '刪除使用者資料', 'Y'),
(7, 'USER', 'EDIT_USER_DATA', 'EditUserData', 'E', '編輯使用者資料', 'Y'),
(8, 'USER', 'SEARCH_USER_DATA', 'SearchUserData', 'S', '搜尋使用者資料', 'Y'),
(9, 'MAINTAIN', 'USER_ROLE_MAINTAIN', 'UserRoleMaintaining', 'ALL', '維護使用者角色權限，具有進入 /users/roles 頁面的權利。', 'Y'),
(10, 'MAINTAIN', 'GROUP_ROLE_MAINTAIN', 'GroupRoleMaintaining', 'ALL', '維護群組角色權限，具有進入 /groups/roles 頁面的權利。', 'Y'),
(11, 'MAINTAIN', 'ROLE_FUNCTION_MAINTAIN', 'RoleFunctionMaintaining', 'ALL', '維護角色功能權限，具有進入 /roles/functions 頁面的權利。', 'Y'),
(12, 'MAINTAIN', 'USER_MAINTAIN', 'UserMaintaining', 'ALL', '維護使用者資料權限，具有進入 /users 頁面的權利。', 'Y'),
(13, 'MAINTAIN', 'ROLE_MAINTAIN', 'RoleMaintaining', 'ALL', '維護角色資料權限，具有進入 /roles 頁面的權利。', 'Y'),
(14, 'MAINTAIN', 'GROUP_MAINTAIN', 'GroupMaintaining', 'ALL', '維護群組資料權限，具有進入 /groups 頁面的權利。', 'Y'),
(15, 'MAINTAIN', 'FUNCTION_MAINTAIN', 'FunctionMaintaining', 'ALL', '維護功能資料權限，具有進入 /functions 頁面的權利。', 'Y'),
(16, 'MAINTAIN', 'SETTING_MAINTAIN', 'SettingMaintaining', 'ALL', '維護自定義設定權限，具有進入 /settings 頁面的權利。', 'Y');
INSERT INTO `setting` (`id`, `data_type`, `type`, `name`, `description`, `priority_no`, `active_flag`) VALUES
(1, 'ROLE', 'ADMIN', '管理者', '系統管理員', 1, 'Y');
INSERT INTO `setting` (`id`, `data_type`, `type`, `name`, `description`, `priority_no`, `active_flag`) VALUES
(2, 'GROUP', 'ADMIN', '管理者群組', '有管理者權限的群組', 1, 'Y');
INSERT INTO `setting` (`id`, `data_type`, `type`, `name`, `description`, `priority_no`, `active_flag`) VALUES
(4, 'GROUP', 'PM', 'PM', 'PM 群組', 2, 'Y');
INSERT INTO `setting` (`id`, `data_type`, `type`, `name`, `description`, `priority_no`, `active_flag`) VALUES
(5, 'GROUP', 'SALES', 'SALES', '業務群組', 1, 'Y'),
(6, 'ROLE', 'SALES', 'Sales', '業務(Sales) 角色類別', 2, 'Y'),
(7, 'ROLE', 'PM', 'PM', 'PM', 3, 'Y'),
(8, 'FUNCTION', 'DASHBOARD', 'Dashboard', 'Dashboard 相關功能', 1, 'Y'),
(9, 'FUNCTION', 'USER', 'User', '使用者相關功能', 2, 'Y'),
(10, 'FUNCTION', 'ROLE', 'Role', '角色相關功能', 3, 'Y'),
(11, 'FUNCTION', 'GROUP', 'Group', '群組相關功能', 4, 'Y'),
(12, 'FUNCTION', 'CUSTOMER', 'Customer', '客戶配置相關功能', 5, 'Y'),
(13, 'FUNCTION', 'SETTING', 'Setting', '設定配置的功能', 6, 'Y'),
(14, 'GROUP', 'GUEST', 'Guest', '訪客群組', 3, 'Y'),
(15, 'ACTION_TYPE', 'B', 'BROWSER', '瀏覽（Browse， 回傳多筆資料的 READ)', 1, 'Y'),
(16, 'ACTION_TYPE', 'R', 'READ', '讀取（Read）', 2, 'Y'),
(17, 'ACTION_TYPE', 'E', 'EDIT', ' 編輯（Edit）', 3, 'Y'),
(18, 'ACTION_TYPE', 'A', 'ADD', '建立（Add）', 4, 'Y'),
(19, 'ACTION_TYPE', 'D', 'DELETE', '刪除（Delete）', 5, 'Y'),
(20, 'ACTION_TYPE', 'S', 'SEARCH', '搜尋（Search）', 6, 'Y'),
(21, 'YES_NO', 'Y', 'Y', 'YES', 1, 'Y'),
(22, 'YES_NO', 'N', 'N', 'NO', 1, 'Y'),
(23, 'LANGUAGE', 'ENGLISH', '英語', '英語語系', 1, 'Y'),
(24, 'LANGUAGE', 'ZH_TW', '繁中', '繁體中文語系', 2, 'Y'),
(25, 'LANGUAGE', 'ZH_CH', '簡中', '簡體中文語系', 3, 'Y'),
(26, 'FUNCTION', 'MAINTAIN', 'Maintain', '維護頁面功能', 1, 'Y'),
(29, 'ACTION_TYPE', 'ALL', 'ALL', '全動作權限', NULL, 'Y');
INSERT INTO `role_function` (`role_id`, `function_id`, `active_flag`) VALUES
(1, 1, 'Y');
INSERT INTO `role_function` (`role_id`, `function_id`, `active_flag`) VALUES
(1, 3, 'Y');
INSERT INTO `role_function` (`role_id`, `function_id`, `active_flag`) VALUES
(1, 4, 'Y');
INSERT INTO `role_function` (`role_id`, `function_id`, `active_flag`) VALUES
(1, 5, 'Y'),
(1, 6, 'Y'),
(1, 7, 'Y'),
(1, 8, 'Y'),
(1, 9, 'Y'),
(1, 10, 'Y'),
(1, 11, 'Y'),
(1, 12, 'Y'),
(1, 13, 'Y'),
(1, 14, 'Y'),
(1, 15, 'Y'),
(1, 16, 'Y');
INSERT INTO `user_role` (`user_id`, `role_id`, `active_flag`) VALUES
( 1, 1, 'Y'),
( 2, 1, 'Y');