# Organizatec — Sistema de Gestão de Pessoas

## 📘 Visão Geral
O **Organizatec** é um sistema web desenvolvido em **Spring Boot** e **Thymeleaf**, voltado à **gestão integrada de funcionários, visitantes e terceiros**.  
Focado em **usabilidade e eficiência**, oferece recursos de controle de acesso, registro de atividades e relatórios diários em uma interface moderna e responsiva.

---

## ⚙️ Principais Funcionalidades

### 👩‍💼 Funcionários Próprios
- Cadastro completo (nome, CPF, matrícula, cargo, salário, data de contratação)  
- Cálculo automático de salário total  
- Associação a departamentos e projetos  
- Registro de ponto e de atividades diárias  

### 🧾 Visitantes
- Registro de visitas (nome, documento, motivo, responsável, setor)  
- Consulta por período ou departamento  
- Relatórios em **PDF** e **CSV**

### 🧑‍🔧 Terceirizados
- Cadastro com função, empresa prestadora, contrato e responsável interno  
- Controle de acesso (entrada e saída)  
- Renovação de vínculo contratual  
- **Validação visual com SweetAlert2** impedindo datas inválidas  

### 📊 Relatórios Diários
- Circulação de pessoas na empresa (funcionários, visitantes e terceiros)  
- Filtros dinâmicos por cargo, setor ou período  
- Exportação rápida em PDF/CSV

### 🧪 Testes Unitários

O sistema possui uma suíte de **testes unitários implementados com JUnit 5 e Mockito**, garantindo a qualidade e o correto funcionamento das principais classes de domínio e serviços.

Os testes cobrem módulos essenciais como **Employee**, **Contractor**, **Visit** e **EmployeeService**, validando cálculos de salário, renovação de contratos, registros de ponto e integridade dos dados.  
Todos os testes estão localizados em: `src/test/java/com/organizatec/peoplemgmt`

---

## Tecnologias Utilizadas
- **Backend:** Java 17, Spring Boot, Spring Data JPA  
- **Frontend:** Thymeleaf, HTML5, CSS3, SweetAlert2  
- **Banco de Dados:** Microsoft SQL Server  
- **Build:** Maven  
