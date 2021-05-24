use [smt_base]
go

-- 初始化项目信息
-- 根虚拟项目
declare @projectId int
insert into base_project(parent_id, root_id, level_, code, name, description, is_virtual, state, create_user_id, create_date, tenant_id)
	values(null, null, 0, 'SMT_ROOT', 'SmartOne虚拟根项目', null, 1, 1, '200ceb26807d6bf99fd6f4f0d1ca54d40001', getdate(), '200ceb26807d6bf99fd6f4f0d1ca54d40001')
set @projectId = ident_current('base_account')
-- web项目
insert into base_project(parent_id, root_id, level_, code, name, description, is_virtual, state, create_user_id, create_date, tenant_id)
	values(@projectId, @projectId, 1, 'SMT_VC', 'SmartOne-可视化项目', null, 0, 1, '200ceb26807d6bf99fd6f4f0d1ca54d40001', getdate(), '200ceb26807d6bf99fd6f4f0d1ca54d40001')
-- 触摸屏项目
insert into base_project(parent_id, root_id, level_, code, name, description, is_virtual, state, create_user_id, create_date, tenant_id)
	values(@projectId, @projectId, 1, 'SMT_TS', 'SmartOne-触摸屏项目', null, 0, 1, '200ceb26807d6bf99fd6f4f0d1ca54d40001', getdate(), '200ceb26807d6bf99fd6f4f0d1ca54d40001')

-- 初始化用户账户信息
-- 管理员
insert into base_user(id, type, name, real_name, is_deleted, create_user_id, create_date, tenant_id)
	values('200ceb26807d6bf99fd6f4f0d1ca54d40001', 0, 'admin', '系统管理员', 0, '200ceb26807d6bf99fd6f4f0d1ca54d40001', getdate(), '200ceb26807d6bf99fd6f4f0d1ca54d40001')
insert into base_account(login_name, login_pwd, is_disabled, user_id, open_user_id, open_date, tenant_id)
	values('admin', 'fac3246df91d74ca659f28f140371d9c', 0, '200ceb26807d6bf99fd6f4f0d1ca54d40001', '200ceb26807d6bf99fd6f4f0d1ca54d40001', getdate(), '200ceb26807d6bf99fd6f4f0d1ca54d40001')
