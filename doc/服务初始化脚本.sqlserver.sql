declare @id char(36)
declare @adminId char(36)
declare @tenantId char(36)

set @adminId= '200ceb26807d6bf99fd6f4f0d1ca54d40001'
set @tenantId= '200ceb26807d6bf99fd6f4f0d1ca54d40002'

-- 初始化项目信息
-- 根虚拟项目
set @id = '0cebbecf-66dd-4d23-a2a7-57c7f5068fc6'
insert into base_project(id, parent_id, root_id, level_, code, name, description, is_virtual, is_default, state, create_user_id, create_date, tenant_id)
	values(@id, null, @id, 0, 'SMT_ROOT', 'SmartOne虚拟根项目', null, 1, 0, 1, @adminId, getdate(), @tenantId)
-- web项目
insert into base_project(id, parent_id, root_id, level_, code, name, description, is_virtual, is_default, state, create_user_id, create_date, tenant_id)
	values('fb377e9b-769a-4697-8a16-b8194131d2e7', @id, @id, 1, 'SMT_VC', 'SmartOne-可视化项目', null, 0, 0, 1, @adminId, getdate(), @tenantId)
-- 触摸屏项目
insert into base_project(id, parent_id, root_id, level_, code, name, description, is_virtual, is_default, state, create_user_id, create_date, tenant_id)
	values('c632873a-05f7-4caf-b49d-b5888edd6013', @id, @id, 1, 'SMT_TS', 'SmartOne-触摸屏项目', null, 0, 0, 1, @adminId, getdate(), @tenantId)
select * from base_project


-- 初始化组织机构信息
set @id = 'c0b3b735-a20a-428a-9b41-7f1a828f27ff'
insert into base_org(id, parent_id, type, code, name, is_deleted, create_user_id, create_date, tenant_id)
	values(@id, null, 1, 'COMPANY', '软件开发公司', 0, @adminId, getdate(), @tenantId)
insert into base_org(id, parent_id, type, code, name, is_deleted, create_user_id, create_date, tenant_id)
	values('67875e6e-effd-4345-a8d6-8614b2037c19', @id, 2, 'RESEARCH', '研发部', 0, @adminId, getdate(), @tenantId)		
select * from base_org


-- 初始化用户账户信息
-- 管理员
insert into base_user(id, type, name, real_name, is_deleted, create_user_id, create_date, tenant_id)
	values(@adminId, 0, 'admin', '系统管理员', 0, @adminId, getdate(), @tenantId)
insert into base_account(login_name, login_pwd, is_disabled, user_id, open_user_id, open_date, tenant_id)
	values('admin', 'fac3246df91d74ca659f28f140371d9c', 0, @adminId, @adminId, getdate(), @tenantId)
select * from base_user

	
-- 初始化角色
-- 管理员角色
insert into base_role(id, code, name, is_deleted, create_user_id, create_date, tenant_id)
	values('7de8f0a0-80f1-48f6-8eb9-a941c7f8aa75', 'administrator', '管理员', 0, @adminId, getdate(), @tenantId)
select * from base_role	






	
