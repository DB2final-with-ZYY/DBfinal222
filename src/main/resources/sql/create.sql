USE DBfinal;

-- 删除院系表（如果存在的话）
DROP TABLE IF EXISTS department;
-- 创建院系表
CREATE TABLE department (
                            department_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,  -- 院系ID，主键，自增
                            department_name VARCHAR(20) NOT NULL,  -- 院系名称，非空
                            address VARCHAR(30),   -- 地址
                            phone_code CHAR(8)     -- 联系电话
);

-- 删除专业表（如果存在的话）
DROP TABLE IF EXISTS major;
-- 创建专业表
CREATE TABLE major (
                       major_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,  -- 专业ID，主键，自增
                       major_name VARCHAR(20) NOT NULL,  -- 专业名称，非空
                       department_id INT NOT NULL,  -- 所属院系
                       FOREIGN KEY (department_id) REFERENCES department(department_id)
);

-- 删除学生表（如果存在的话）
DROP TABLE IF EXISTS student;
-- 创建学生表
CREATE TABLE student (
                         student_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, -- 自增主键
                         password CHAR(30) NOT NULL,   -- 密码非空
                         email VARCHAR(100) UNIQUE,    -- 邮箱，唯一
                         name VARCHAR(30) NOT NULL,    -- 姓名，非空
                         gender ENUM('M', 'F', 'O') DEFAULT 'O',  -- 性别，枚举类型，默认 'O'
                         native_place VARCHAR(10),  -- 生源地
                         department_id INT NOT NULL,  -- 院系号
                         major_id INT NOT NULL,  -- 专业ID
                         status ENUM('正常', '试读', '休学', '毕业') DEFAULT '正常', -- 学生状态
                         FOREIGN KEY (department_id) REFERENCES department(department_id),
                         FOREIGN KEY (major_id) REFERENCES major(major_id)
);

-- 删除教师表（如果存在的话）
DROP TABLE IF EXISTS teacher;
-- 创建教师表
CREATE TABLE teacher (
                         teacher_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,  -- 教师ID，主键，自增
                         name VARCHAR(30) NOT NULL,  -- 教师姓名，非空
                         email VARCHAR(100) UNIQUE,    -- 邮箱，唯一
                         position ENUM('正教授', '副教授', '高级工程师', '讲师') DEFAULT '讲师',   -- 职位
                         gender ENUM('M', 'F', 'O') DEFAULT 'O',  -- 性别
                         department_id INT NOT NULL,  -- 所属院系
                         FOREIGN KEY (department_id) REFERENCES department(department_id)
);



-- 删除课程表（如果存在的话）
DROP TABLE IF EXISTS course;
-- 创建课程表
CREATE TABLE course (
                        course_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,  -- 课程ID，主键，自增
                        course_name VARCHAR(50) NOT NULL,  -- 课程名称，非空
                        department_id INT NOT NULL,  -- 所属院系
                        credit INT NOT NULL,  -- 学分
                        FOREIGN KEY (department_id) REFERENCES department(department_id)
);

-- 删除选课方案表（如果存在的话）
DROP TABLE IF EXISTS course_plan;
-- 创建选课方案表，记录专业与对应课程的关系
CREATE TABLE course_plan (
                             plan_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,  -- 方案ID，主键，自增
                             major_id INT NOT NULL,  -- 专业ID
                             course_id INT NOT NULL,  -- 课程ID
                             FOREIGN KEY (major_id) REFERENCES major(major_id),
                             FOREIGN KEY (course_id) REFERENCES course(course_id)
);


-- 删除课程开课安排表（如果存在的话）
DROP TABLE IF EXISTS class_schedule;
-- 创建课程开课安排表
CREATE TABLE class_schedule (
                                schedule_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,  -- 排课ID，主键，自增
                                semester INT NOT NULL, -- 学期 202501这种格式
                                course_id INT NOT NULL,  -- 课程ID
                                teacher_id INT NOT NULL,  -- 教师ID
                                classroom VARCHAR(30),  -- 教室
                                capacity INT NOT NULL,  -- 容量 人数
                                class_time VARCHAR(10), -- 开课时间 格式：星期几几到几
                                FOREIGN KEY (course_id) REFERENCES course(course_id),
                                FOREIGN KEY (teacher_id) REFERENCES teacher(teacher_id)
);

-- 删除选课表（如果存在的话）
DROP TABLE IF EXISTS enrollment;
-- 创建选课表
CREATE TABLE enrollment (
                            enrollment_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,  -- 选课ID，主键，自增
                            student_id INT NOT NULL,  -- 学生ID
                            schedule_id INT NOT NULL,  -- 课程开设表id
                            FOREIGN KEY (student_id) REFERENCES student(student_id),
                            FOREIGN KEY (schedule_id) REFERENCES class_schedule(schedule_id)
);

-- 删除成绩表（如果存在的话）
DROP TABLE IF EXISTS grade;
-- 创建成绩表
CREATE TABLE grade (
                       grade_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,  -- 成绩ID，主键，自增
                       enrollment_id INT NOT NULL,  -- 选课id
                       grade DECIMAL(5,2),  -- 成绩
                       exam_date DATE,  -- 考试日期
                       FOREIGN KEY (enrollment_id) REFERENCES enrollment(enrollment_id)
);
