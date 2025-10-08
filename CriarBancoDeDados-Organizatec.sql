/* === Selecione o banco correto === */
-- USE Organizatec;
-- GO

/* ========= DROP em ordem segura (filhos -> pais) ========= */
IF OBJECT_ID('dbo.employee_projects','U') IS NOT NULL DROP TABLE dbo.employee_projects;
IF OBJECT_ID('dbo.time_entries','U') IS NOT NULL DROP TABLE dbo.time_entries;
IF OBJECT_ID('dbo.employee_activities','U') IS NOT NULL DROP TABLE dbo.employee_activities;
IF OBJECT_ID('dbo.employee_role_history','U') IS NOT NULL DROP TABLE dbo.employee_role_history;
IF OBJECT_ID('dbo.visits','U') IS NOT NULL DROP TABLE dbo.visits;
IF OBJECT_ID('dbo.contractors','U') IS NOT NULL DROP TABLE dbo.contractors;
IF OBJECT_ID('dbo.contractor_departments','U') IS NOT NULL DROP TABLE dbo.contractor_departments;
IF OBJECT_ID('dbo.contractor_access','U') IS NOT NULL DROP TABLE dbo.contractor_access;


IF OBJECT_ID('dbo.projects','U') IS NOT NULL DROP TABLE dbo.projects;
IF OBJECT_ID('dbo.visitors','U') IS NOT NULL DROP TABLE dbo.visitors;
/* Se quiser recriar base inteira, pode dropar também employees/departments.
   Se NÃO quiser perder funcionários/departamentos, comente as duas linhas abaixo. */
IF OBJECT_ID('dbo.employees','U') IS NOT NULL DROP TABLE dbo.employees;
IF OBJECT_ID('dbo.departments','U') IS NOT NULL DROP TABLE dbo.departments;

PRINT('Tabelas antigas removidas');

/* ========= CREATE ========= */

/* Departments */
CREATE TABLE departments (
  id   BIGINT IDENTITY PRIMARY KEY,
  name VARCHAR(100) NOT NULL UNIQUE
);

/* Employees */
CREATE TABLE employees (
  id            BIGINT IDENTITY PRIMARY KEY,
  name          VARCHAR(150) NOT NULL,
  cpf           VARCHAR(14)  NOT NULL UNIQUE, -- com máscara (já validamos no app)
  birth_date    DATE         NOT NULL,
  enrollment    VARCHAR(40)  NOT NULL UNIQUE,
  role_title    VARCHAR(100) NOT NULL,
  base_salary   DECIMAL(18,2) NOT NULL,
  hired_at      DATE          NOT NULL,
  department_id BIGINT        NULL
    CONSTRAINT fk_emp_dept REFERENCES departments(id),
  created_at    DATETIME2     NOT NULL DEFAULT SYSDATETIME()
);

/* Projects */
CREATE TABLE projects (
  id           BIGINT IDENTITY PRIMARY KEY,
  name         VARCHAR(120) NOT NULL,
  description  VARCHAR(500) NULL
);

/* Employee x Projects (N:N) */
CREATE TABLE employee_projects (
  employee_id BIGINT NOT NULL
    CONSTRAINT fk_empprj_emp REFERENCES employees(id) ON DELETE CASCADE,
  project_id  BIGINT NOT NULL
    CONSTRAINT fk_empprj_prj REFERENCES projects(id)  ON DELETE CASCADE,
  CONSTRAINT pk_empprj PRIMARY KEY (employee_id, project_id)
);

/* Histórico de Cargos */
CREATE TABLE employee_role_history (
  id            BIGINT IDENTITY PRIMARY KEY,
  employee_id   BIGINT NOT NULL
    CONSTRAINT fk_erh_emp REFERENCES employees(id) ON DELETE CASCADE,
  role_title    VARCHAR(100) NOT NULL,
  base_salary   DECIMAL(18,2) NOT NULL,
  start_date    DATE NOT NULL,
  end_date      DATE NULL,
  change_reason VARCHAR(200) NULL
);

CREATE TABLE dbo.employee_activities (
	id             BIGINT IDENTITY(1,1) PRIMARY KEY,
	employee_id    BIGINT       NOT NULL
		CONSTRAINT fk_empact_emp REFERENCES dbo.employees(id) ON DELETE CASCADE,
	title          VARCHAR(150) NOT NULL,
	description    VARCHAR(1000) NULL,
	started_at     DATETIME2    NOT NULL,
	finished_at    DATETIME2    NULL,
	created_at     DATETIME2    NOT NULL DEFAULT SYSDATETIME()
);

/* Batidas de Ponto */
CREATE TABLE time_entries (
  id          BIGINT IDENTITY PRIMARY KEY,
  employee_id BIGINT NOT NULL
    CONSTRAINT fk_te_emp REFERENCES employees(id) ON DELETE CASCADE,
  punch_type  VARCHAR(10) NOT NULL, -- IN/OUT
  occurred_at DATETIME2   NOT NULL DEFAULT SYSDATETIME()
);

/* Visitors (separado do Visit — cadastro de pessoas externas) */
CREATE TABLE visitors (
  id           BIGINT IDENTITY PRIMARY KEY,
  name         VARCHAR(150) NOT NULL,
  document_id  VARCHAR(20)  NULL,
  company      VARCHAR(100) NULL,
  created_at   DATETIME2    NOT NULL DEFAULT SYSDATETIME()
);

/* Visits (registros de entrada/saída) */
CREATE TABLE visits (
  id               BIGINT IDENTITY PRIMARY KEY,
  visitor_id       BIGINT NULL
    CONSTRAINT fk_vis_visitors REFERENCES visitors(id) ON DELETE CASCADE,
  visited_dept_id  BIGINT NULL
    CONSTRAINT fk_vis_dept REFERENCES departments(id),
  visited_emp_id   BIGINT NULL
    CONSTRAINT fk_vis_emp REFERENCES employees(id),
  entry_time       DATETIME2 NOT NULL,
  exit_time        DATETIME2 NULL,
  badge_code       VARCHAR(24) NOT NULL,
  company		   VARCHAR(100) NULL,
  visitor_name     VARCHAR(150) NULL,
  document_id      VARCHAR(20)  NULL,
  reason           VARCHAR(500) NULL,
  vehicle_plate    VARCHAR(10)  NULL,
  created_at       DATETIME2    NOT NULL DEFAULT SYSDATETIME()
);

CREATE TABLE contractors (
  id               BIGINT IDENTITY PRIMARY KEY,
  name             VARCHAR(150)  NOT NULL,
  cpf              VARCHAR(14)   NOT NULL UNIQUE,  -- com máscara
  role_title       VARCHAR(100)  NOT NULL,
  vendor_company   VARCHAR(120)  NOT NULL,
  contract_start   DATE          NOT NULL,
  contract_end     DATE          NULL,
  internal_resp_id BIGINT        NULL
    CONSTRAINT fk_contractor_resp REFERENCES employees(id),
  badge_code       VARCHAR(20)   NULL,
  active_flag      BIT           NOT NULL DEFAULT 1
);

CREATE TABLE contractor_departments (
  contractor_id BIGINT NOT NULL
    CONSTRAINT fk_cd_contractor REFERENCES contractors(id) ON DELETE CASCADE,
  department_id BIGINT NOT NULL
    CONSTRAINT fk_cd_department REFERENCES departments(id),
  CONSTRAINT pk_contractor_departments PRIMARY KEY (contractor_id, department_id)
);

CREATE TABLE dbo.contractor_access (
  id            BIGINT IDENTITY PRIMARY KEY,
  contractor_id BIGINT NOT NULL
    CONSTRAINT fk_ca_contractor REFERENCES contractors(id) ON DELETE CASCADE,
  type          VARCHAR(8) NOT NULL,     -- IN / OUT
  event_at      DATETIME2 NOT NULL DEFAULT SYSDATETIME()
);

/* ========= Índices úteis ========= */
CREATE INDEX ix_emp_dept ON employees(department_id);
CREATE INDEX ix_vis_emp  ON visits(visited_emp_id);
CREATE INDEX ix_vis_dept ON visits(visited_dept_id);
CREATE INDEX ix_vis_time ON visits(entry_time);
CREATE INDEX IX_empact_emp_started ON dbo.employee_activities(employee_id, started_at DESC);

/* ========= Seeds mínimos (opcional) ========= */
IF NOT EXISTS (SELECT 1 FROM departments) INSERT INTO departments(name) VALUES ('Administração'), ('Tecnologia'), ('Operações'), ('RH');

-- Exemplo de projeto
IF NOT EXISTS (SELECT 1 FROM projects) INSERT INTO projects(name, description) VALUES ('Intranet 2.0','Portal interno da organização');

PRINT('Schema criado com sucesso');