USE organizatec;
GO

IF COL_LENGTH('departments','created_at') IS NULL
  ALTER TABLE departments ADD created_at DATETIME2 NOT NULL CONSTRAINT DF_departments_created_at DEFAULT SYSDATETIME();
IF COL_LENGTH('projects','created_at') IS NULL
  ALTER TABLE projects ADD created_at DATETIME2 NOT NULL CONSTRAINT DF_projects_created_at DEFAULT SYSDATETIME();
IF COL_LENGTH('employees','created_at') IS NULL
  ALTER TABLE employees ADD created_at DATETIME2 NOT NULL CONSTRAINT DF_employees_created_at DEFAULT SYSDATETIME();
IF COL_LENGTH('employee_role_history','created_at') IS NULL
  ALTER TABLE employee_role_history ADD created_at DATETIME2 NOT NULL CONSTRAINT DF_emp_role_hist_created_at DEFAULT SYSDATETIME();
IF COL_LENGTH('time_entries','created_at') IS NULL
  ALTER TABLE time_entries ADD created_at DATETIME2 NOT NULL CONSTRAINT DF_time_entries_created_at DEFAULT SYSDATETIME();
IF COL_LENGTH('activities','created_at') IS NULL
  ALTER TABLE activities ADD created_at DATETIME2 NOT NULL CONSTRAINT DF_activities_created_at DEFAULT SYSDATETIME();
IF COL_LENGTH('contractors','created_at') IS NULL
  ALTER TABLE contractors ADD created_at DATETIME2 NOT NULL CONSTRAINT DF_contractors_created_at DEFAULT SYSDATETIME();
IF COL_LENGTH('contractor_access','created_at') IS NULL
  ALTER TABLE contractor_access ADD created_at DATETIME2 NOT NULL CONSTRAINT DF_contractor_access_created_at DEFAULT SYSDATETIME();
IF COL_LENGTH('visitors','created_at') IS NULL
  ALTER TABLE visitors ADD created_at DATETIME2 NOT NULL CONSTRAINT DF_visitors_created_at DEFAULT SYSDATETIME();
IF COL_LENGTH('visits','created_at') IS NULL
  ALTER TABLE visits ADD created_at DATETIME2 NOT NULL CONSTRAINT DF_visits_created_at DEFAULT SYSDATETIME();
GO

