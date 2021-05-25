declare @parentId int
declare @adminId char(36)
declare @tenantId char(36)

set @adminId= '200ceb26807d6bf99fd6f4f0d1ca54d40001'
set @tenantId= '200ceb26807d6bf99fd6f4f0d1ca54d40002'

-- 初始化项目信息
-- 根虚拟项目
insert into base_project(parent_id, root_id, level_, code, name, description, is_virtual, state, create_user_id, create_date, tenant_id)
	values(null, null, 0, 'SMT_ROOT', 'SmartOne虚拟根项目', null, 1, 1, @adminId, getdate(), @tenantId)
set @parentId = ident_current('base_account')
-- web项目
insert into base_project(parent_id, root_id, level_, code, name, description, is_virtual, state, create_user_id, create_date, tenant_id)
	values(@parentId, @parentId, 1, 'SMT_VC', 'SmartOne-可视化项目', null, 0, 1, @adminId, getdate(), @tenantId)
-- 触摸屏项目
insert into base_project(parent_id, root_id, level_, code, name, description, is_virtual, state, create_user_id, create_date, tenant_id)
	values(@parentId, @parentId, 1, 'SMT_TS', 'SmartOne-触摸屏项目', null, 0, 1, @adminId, getdate(), @tenantId)
select * from base_project


-- 初始化组织机构信息
insert into base_org(parent_id, type, code, name, is_deleted, create_user_id, create_date, tenant_id)
	values(null, 1, 'COMPANY', '软件开发公司', 0, @adminId, getdate(), @tenantId)
set @parentId = ident_current('base_org')
insert into base_org(parent_id, type, code, name, is_deleted, create_user_id, create_date, tenant_id)
	values(@parentId, 2, 'RESEARCH', '研发部', 0, @adminId, getdate(), @tenantId)		
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
insert into base_role(code, name, is_deleted, create_user_id, create_date, tenant_id)
	values('administrator', '管理员', 0, @adminId, getdate(), @tenantId)
select * from base_role	






	
