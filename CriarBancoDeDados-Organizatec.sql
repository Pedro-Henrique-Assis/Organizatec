-- 01) DB
IF DB_ID('organizatec') IS NULL CREATE DATABASE organizatec;
GO
USE organizatec;
GO

-- 02) Tabelas base
CREATE TABLE departments (
  id           BIGINT IDENTITY PRIMARY KEY,
  name         VARCHAR(120) NOT NULL UNIQUE
);

CREATE TABLE projects (
  id           BIGINT IDENTITY PRIMARY KEY,
  name         VARCHAR(120) NOT NULL UNIQUE
);

-- 03) Funcionários
CREATE TABLE employees (
  id              BIGINT IDENTITY PRIMARY KEY,
  name            VARCHAR(150) NOT NULL,
  cpf             VARCHAR(14)  NOT NULL UNIQUE,
  birth_date      DATE         NOT NULL,
  enrollment      VARCHAR(32)  NOT NULL UNIQUE, -- matrícula gerada automaticamente
  role_title      VARCHAR(100) NOT NULL,
  base_salary     DECIMAL(12,2) NOT NULL,
  hired_at        DATE          NOT NULL,
  department_id   BIGINT        NOT NULL
    CONSTRAINT fk_emp_dept REFERENCES departments(id)
);

CREATE TABLE employee_role_history (
  id           BIGINT IDENTITY PRIMARY KEY,
  employee_id  BIGINT NOT NULL
    CONSTRAINT fk_rolehist_emp REFERENCES employees(id) ON DELETE CASCADE,
  role_title   VARCHAR(100) NOT NULL,
  start_date   DATE NOT NULL,
  end_date     DATE NULL
);

CREATE TABLE employee_projects (
  employee_id BIGINT NOT NULL
    CONSTRAINT fk_ep_emp REFERENCES employees(id) ON DELETE CASCADE,
  project_id  BIGINT NOT NULL
    CONSTRAINT fk_ep_proj REFERENCES projects(id) ON DELETE CASCADE,
  CONSTRAINT pk_ep PRIMARY KEY (employee_id, project_id)
);

CREATE TABLE time_entries (
  id           BIGINT IDENTITY PRIMARY KEY,
  employee_id  BIGINT NOT NULL
    CONSTRAINT fk_time_emp REFERENCES employees(id) ON DELETE CASCADE,
  punch_time   DATETIME2 NOT NULL,
  punch_type   VARCHAR(8) NOT NULL CHECK (punch_type IN ('IN','OUT'))
);

CREATE TABLE activities (
  id           BIGINT IDENTITY PRIMARY KEY,
  employee_id  BIGINT NOT NULL
    CONSTRAINT fk_act_emp REFERENCES employees(id) ON DELETE CASCADE,
  description  VARCHAR(400) NOT NULL,
  created_at   DATETIME2 NOT NULL DEFAULT SYSDATETIME()
);

-- 04) Terceirizados
CREATE TABLE contractors (
  id                 BIGINT IDENTITY PRIMARY KEY,
  name               VARCHAR(150) NOT NULL,
  cpf                VARCHAR(14)  NOT NULL UNIQUE,
  function_title     VARCHAR(100) NOT NULL,
  provider_company   VARCHAR(120) NOT NULL,
  contract_start     DATE NOT NULL,
  contract_end       DATE NULL,
  internal_resp_id   BIGINT NULL
    CONSTRAINT fk_contr_resp REFERENCES employees(id)
);

CREATE TABLE contractor_departments (
  contractor_id BIGINT NOT NULL
    CONSTRAINT fk_cd_contr REFERENCES contractors(id) ON DELETE CASCADE,
  department_id BIGINT NOT NULL
    CONSTRAINT fk_cd_dept REFERENCES departments(id) ON DELETE CASCADE,
  CONSTRAINT pk_cd PRIMARY KEY (contractor_id, department_id)
);

CREATE TABLE contractor_access (
  id             BIGINT IDENTITY PRIMARY KEY,
  contractor_id  BIGINT NOT NULL
    CONSTRAINT fk_cacc_contr REFERENCES contractors(id) ON DELETE CASCADE,
  entry_time     DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
  exit_time      DATETIME2 NULL
);

-- 05) Visitantes
CREATE TABLE visitors (
  id            BIGINT IDENTITY PRIMARY KEY,
  name          VARCHAR(150) NOT NULL,
  document_id   VARCHAR(40)  NOT NULL,
  reason        VARCHAR(200) NOT NULL
);

CREATE TABLE visits (
  id                BIGINT IDENTITY PRIMARY KEY,
  visitor_id        BIGINT NOT NULL
    CONSTRAINT fk_vis_visitors REFERENCES visitors(id) ON DELETE CASCADE,
  visited_dept_id   BIGINT NULL
    CONSTRAINT fk_vis_dept REFERENCES departments(id),
  visited_emp_id    BIGINT NULL
    CONSTRAINT fk_vis_emp REFERENCES employees(id),
  entry_time        DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
  exit_time         DATETIME2 NULL,
  badge_code        VARCHAR(24) NOT NULL
);

-- 06) Índices úteis
CREATE INDEX ix_emp_dept ON employees(department_id);
CREATE INDEX ix_vis_period ON visits(entry_time, exit_time);
CREATE INDEX ix_time_entries_emp ON time_entries(employee_id, punch_time);

-- 07) Seeds mínimos
INSERT INTO departments(name) VALUES ('RH'),('Segurança'),('TI'),('Operações');
INSERT INTO projects(name) VALUES ('Intranet'),('ERP'),('Reforma Sede');