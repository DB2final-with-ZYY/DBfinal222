
-- 操作日志表（请先创建）
CREATE TABLE table_change_log (
    log_id INT PRIMARY KEY AUTO_INCREMENT,
    table_name VARCHAR(50),
    operation_type ENUM('INSERT', 'UPDATE', 'DELETE'),
    changed_data TEXT,
    operation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



-- department 表 INSERT 触发器
CREATE TRIGGER trg_department_insert
AFTER INSERT ON department
FOR EACH ROW
INSERT INTO table_change_log (table_name, operation_type, changed_data)
VALUES (
    'department', 'INSERT',
    CONCAT('NEW => department_id: ', NEW.department_id, ', department_name: ', NEW.department_name,
           ', address: ', NEW.address, ', phone_code: ', NEW.phone_code)
);

-- department 表 UPDATE 触发器
CREATE TRIGGER trg_department_update
AFTER UPDATE ON department
FOR EACH ROW
INSERT INTO table_change_log (table_name, operation_type, changed_data)
VALUES (
    'department', 'UPDATE',
    CONCAT('OLD => department_id: ', OLD.department_id, ', department_name: ', OLD.department_name,
           ', address: ', OLD.address, ', phone_code: ', OLD.phone_code,
           ' || NEW => department_id: ', NEW.department_id, ', department_name: ', NEW.department_name,
           ', address: ', NEW.address, ', phone_code: ', NEW.phone_code)
);

-- department 表 DELETE 触发器
CREATE TRIGGER trg_department_delete
AFTER DELETE ON department
FOR EACH ROW
INSERT INTO table_change_log (table_name, operation_type, changed_data)
VALUES (
    'department', 'DELETE',
    CONCAT('OLD => department_id: ', OLD.department_id, ', department_name: ', OLD.department_name,
           ', address: ', OLD.address, ', phone_code: ', OLD.phone_code)
);


-- student 表 INSERT 触发器
CREATE TRIGGER trg_student_insert
AFTER INSERT ON student
FOR EACH ROW
INSERT INTO table_change_log (table_name, operation_type, changed_data)
VALUES (
    'student', 'INSERT',
    CONCAT('NEW => student_id: ', NEW.student_id, ', name: ', NEW.name,
           ', email: ', NEW.email, ', gender: ', NEW.gender,
           ', grade_number: ', NEW.grade_number, ', native_place: ', NEW.native_place,
           ', department_id: ', NEW.department_id, ', major_id: ', NEW.major_id,
           ', status: ', NEW.status)
);

-- student 表 UPDATE 触发器
CREATE TRIGGER trg_student_update
AFTER UPDATE ON student
FOR EACH ROW
INSERT INTO table_change_log (table_name, operation_type, changed_data)
VALUES (
    'student', 'UPDATE',
    CONCAT('OLD => student_id: ', OLD.student_id, ', name: ', OLD.name,
           ', email: ', OLD.email, ', gender: ', OLD.gender,
           ', grade_number: ', OLD.grade_number, ', native_place: ', OLD.native_place,
           ', department_id: ', OLD.department_id, ', major_id: ', OLD.major_id,
           ', status: ', OLD.status,
           ' || NEW => student_id: ', NEW.student_id, ', name: ', NEW.name,
           ', email: ', NEW.email, ', gender: ', NEW.gender,
           ', grade_number: ', NEW.grade_number, ', native_place: ', NEW.native_place,
           ', department_id: ', NEW.department_id, ', major_id: ', NEW.major_id,
           ', status: ', NEW.status)
);

-- student 表 DELETE 触发器
CREATE TRIGGER trg_student_delete
AFTER DELETE ON student
FOR EACH ROW
INSERT INTO table_change_log (table_name, operation_type, changed_data)
VALUES (
    'student', 'DELETE',
    CONCAT('OLD => student_id: ', OLD.student_id, ', name: ', OLD.name,
           ', email: ', OLD.email, ', gender: ', OLD.gender,
           ', grade_number: ', OLD.grade_number, ', native_place: ', OLD.native_place,
           ', department_id: ', OLD.department_id, ', major_id: ', OLD.major_id,
           ', status: ', OLD.status)
);


CREATE TRIGGER trg_major_insert
AFTER INSERT ON major
FOR EACH ROW
INSERT INTO table_change_log(table_name, operation_type, changed_data)
VALUES ('major', 'INSERT',
CONCAT('NEW => major_id: ', NEW.major_id, ', major_name: ', NEW.major_name, ', department_id: ', NEW.department_id));

CREATE TRIGGER trg_major_update
AFTER UPDATE ON major
FOR EACH ROW
INSERT INTO table_change_log(table_name, operation_type, changed_data)
VALUES ('major', 'UPDATE',
CONCAT('OLD => major_id: ', OLD.major_id, ', major_name: ', OLD.major_name, ', department_id: ', OLD.department_id,
       ' || NEW => major_id: ', NEW.major_id, ', major_name: ', NEW.major_name, ', department_id: ', NEW.department_id));

CREATE TRIGGER trg_major_delete
AFTER DELETE ON major
FOR EACH ROW
INSERT INTO table_change_log(table_name, operation_type, changed_data)
VALUES ('major', 'DELETE',
CONCAT('OLD => major_id: ', OLD.major_id, ', major_name: ', OLD.major_name, ', department_id: ', OLD.department_id));


CREATE TRIGGER trg_teacher_insert
AFTER INSERT ON teacher
FOR EACH ROW
INSERT INTO table_change_log(table_name, operation_type, changed_data)
VALUES ('teacher', 'INSERT',
CONCAT('NEW => teacher_id: ', NEW.teacher_id, ', name: ', NEW.name, ', password: ', NEW.password,
       ', email: ', NEW.email, ', position: ', NEW.position, ', gender: ', NEW.gender,
       ', department_id: ', NEW.department_id, ', status: ', NEW.status));

CREATE TRIGGER trg_teacher_update
AFTER UPDATE ON teacher
FOR EACH ROW
INSERT INTO table_change_log(table_name, operation_type, changed_data)
VALUES ('teacher', 'UPDATE',
CONCAT('OLD => teacher_id: ', OLD.teacher_id, ', name: ', OLD.name, ', password: ', OLD.password,
       ', email: ', OLD.email, ', position: ', OLD.position, ', gender: ', OLD.gender,
       ', department_id: ', OLD.department_id, ', status: ', OLD.status,
       ' || NEW => teacher_id: ', NEW.teacher_id, ', name: ', NEW.name, ', password: ', NEW.password,
       ', email: ', NEW.email, ', position: ', NEW.position, ', gender: ', NEW.gender,
       ', department_id: ', NEW.department_id, ', status: ', NEW.status));

CREATE TRIGGER trg_teacher_delete
AFTER DELETE ON teacher
FOR EACH ROW
INSERT INTO table_change_log(table_name, operation_type, changed_data)
VALUES ('teacher', 'DELETE',
CONCAT('OLD => teacher_id: ', OLD.teacher_id, ', name: ', OLD.name, ', password: ', OLD.password,
       ', email: ', OLD.email, ', position: ', OLD.position, ', gender: ', OLD.gender,
       ', department_id: ', OLD.department_id, ', status: ', OLD.status));



CREATE TRIGGER trg_admin_insert
AFTER INSERT ON admin
FOR EACH ROW
INSERT INTO table_change_log(table_name, operation_type, changed_data)
VALUES ('admin', 'INSERT',
CONCAT('NEW => admin_id: ', NEW.admin_id, ', name: ', NEW.name, ', password: ', NEW.password,
       ', email: ', NEW.email, ', gender: ', NEW.gender, ', department_id: ', NEW.department_id));

CREATE TRIGGER trg_admin_update
AFTER UPDATE ON admin
FOR EACH ROW
INSERT INTO table_change_log(table_name, operation_type, changed_data)
VALUES ('admin', 'UPDATE',
CONCAT('OLD => admin_id: ', OLD.admin_id, ', name: ', OLD.name, ', password: ', OLD.password,
       ', email: ', OLD.email, ', gender: ', OLD.gender, ', department_id: ', OLD.department_id,
       ' || NEW => admin_id: ', NEW.admin_id, ', name: ', NEW.name, ', password: ', NEW.password,
       ', email: ', NEW.email, ', gender: ', NEW.gender, ', department_id: ', NEW.department_id));

CREATE TRIGGER trg_admin_delete
AFTER DELETE ON admin
FOR EACH ROW
INSERT INTO table_change_log(table_name, operation_type, changed_data)
VALUES ('admin', 'DELETE',
CONCAT('OLD => admin_id: ', OLD.admin_id, ', name: ', OLD.name, ', password: ', OLD.password,
       ', email: ', OLD.email, ', gender: ', OLD.gender, ', department_id: ', OLD.department_id));



CREATE TRIGGER trg_course_insert
AFTER INSERT ON course
FOR EACH ROW
INSERT INTO table_change_log(table_name, operation_type, changed_data)
VALUES ('course', 'INSERT',
CONCAT('NEW => course_id: ', NEW.course_id, ', course_name: ', NEW.course_name, ', department_id: ', NEW.department_id,
       ', credit: ', NEW.credit, ', exam_weight: ', NEW.exam_weight, ', status: ', NEW.status));

CREATE TRIGGER trg_course_update
AFTER UPDATE ON course
FOR EACH ROW
INSERT INTO table_change_log(table_name, operation_type, changed_data)
VALUES ('course', 'UPDATE',
CONCAT('OLD => course_id: ', OLD.course_id, ', course_name: ', OLD.course_name, ', department_id: ', OLD.department_id,
       ', credit: ', OLD.credit, ', exam_weight: ', OLD.exam_weight, ', status: ', OLD.status,
       ' || NEW => course_id: ', NEW.course_id, ', course_name: ', NEW.course_name, ', department_id: ', NEW.department_id,
       ', credit: ', NEW.credit, ', exam_weight: ', NEW.exam_weight, ', status: ', NEW.status));

CREATE TRIGGER trg_course_delete
AFTER DELETE ON course
FOR EACH ROW
INSERT INTO table_change_log(table_name, operation_type, changed_data)
VALUES ('course', 'DELETE',
CONCAT('OLD => course_id: ', OLD.course_id, ', course_name: ', OLD.course_name, ', department_id: ', OLD.department_id,
       ', credit: ', OLD.credit, ', exam_weight: ', OLD.exam_weight, ', status: ', OLD.status));


CREATE TRIGGER trg_course_plan_insert
AFTER INSERT ON course_plan
FOR EACH ROW
INSERT INTO table_change_log(table_name, operation_type, changed_data)
VALUES ('course_plan', 'INSERT',
CONCAT('NEW => plan_id: ', NEW.plan_id, ', major_id: ', NEW.major_id, ', course_id: ', NEW.course_id));

CREATE TRIGGER trg_course_plan_update
AFTER UPDATE ON course_plan
FOR EACH ROW
INSERT INTO table_change_log(table_name, operation_type, changed_data)
VALUES ('course_plan', 'UPDATE',
CONCAT('OLD => plan_id: ', OLD.plan_id, ', major_id: ', OLD.major_id, ', course_id: ', OLD.course_id,
       ' || NEW => plan_id: ', NEW.plan_id, ', major_id: ', NEW.major_id, ', course_id: ', NEW.course_id));

CREATE TRIGGER trg_course_plan_delete
AFTER DELETE ON course_plan
FOR EACH ROW
INSERT INTO table_change_log(table_name, operation_type, changed_data)
VALUES ('course_plan', 'DELETE',
CONCAT('OLD => plan_id: ', OLD.plan_id, ', major_id: ', OLD.major_id, ', course_id: ', OLD.course_id));


CREATE TRIGGER trg_class_schedule_insert
AFTER INSERT ON class_schedule
FOR EACH ROW
INSERT INTO table_change_log(table_name, operation_type, changed_data)
VALUES ('class_schedule', 'INSERT',
CONCAT('NEW => schedule_id: ', NEW.schedule_id, ', semester: ', NEW.semester, ', course_id: ', NEW.course_id,
       ', teacher_id: ', NEW.teacher_id, ', classroom: ', NEW.classroom, ', capacity: ', NEW.capacity,
       ', class_time: ', NEW.class_time, ', status: ', NEW.status));

CREATE TRIGGER trg_class_schedule_update
AFTER UPDATE ON class_schedule
FOR EACH ROW
INSERT INTO table_change_log(table_name, operation_type, changed_data)
VALUES ('class_schedule', 'UPDATE',
CONCAT('OLD => schedule_id: ', OLD.schedule_id, ', semester: ', OLD.semester, ', course_id: ', OLD.course_id,
       ', teacher_id: ', OLD.teacher_id, ', classroom: ', OLD.classroom, ', capacity: ', OLD.capacity,
       ', class_time: ', OLD.class_time, ', status: ', OLD.status,
       ' || NEW => schedule_id: ', NEW.schedule_id, ', semester: ', NEW.semester, ', course_id: ', NEW.course_id,
       ', teacher_id: ', NEW.teacher_id, ', classroom: ', NEW.classroom, ', capacity: ', NEW.capacity,
       ', class_time: ', NEW.class_time, ', status: ', NEW.status));

CREATE TRIGGER trg_class_schedule_delete
AFTER DELETE ON class_schedule
FOR EACH ROW
INSERT INTO table_change_log(table_name, operation_type, changed_data)
VALUES ('class_schedule', 'DELETE',
CONCAT('OLD => schedule_id: ', OLD.schedule_id, ', semester: ', OLD.semester, ', course_id: ', OLD.course_id,
       ', teacher_id: ', OLD.teacher_id, ', classroom: ', OLD.classroom, ', capacity: ', OLD.capacity,
       ', class_time: ', OLD.class_time, ', status: ', OLD.status));



CREATE TRIGGER trg_enrollment_insert
AFTER INSERT ON enrollment
FOR EACH ROW
INSERT INTO table_change_log(table_name, operation_type, changed_data)
VALUES ('enrollment', 'INSERT',
CONCAT('NEW => enrollment_id: ', NEW.enrollment_id, ', student_id: ', NEW.student_id, ', schedule_id: ', NEW.schedule_id));

CREATE TRIGGER trg_enrollment_update
AFTER UPDATE ON enrollment
FOR EACH ROW
INSERT INTO table_change_log(table_name, operation_type, changed_data)
VALUES ('enrollment', 'UPDATE',
CONCAT('OLD => enrollment_id: ', OLD.enrollment_id, ', student_id: ', OLD.student_id, ', schedule_id: ', OLD.schedule_id,
       ' || NEW => enrollment_id: ', NEW.enrollment_id, ', student_id: ', NEW.student_id, ', schedule_id: ', NEW.schedule_id));

CREATE TRIGGER trg_enrollment_delete
AFTER DELETE ON enrollment
FOR EACH ROW
INSERT INTO table_change_log(table_name, operation_type, changed_data)
VALUES ('enrollment', 'DELETE',
CONCAT('OLD => enrollment_id: ', OLD.enrollment_id, ', student_id: ', OLD.student_id, ', schedule_id: ', OLD.schedule_id));


CREATE TRIGGER trg_grade_insert
AFTER INSERT ON grade
FOR EACH ROW
INSERT INTO table_change_log(table_name, operation_type, changed_data)
VALUES ('grade', 'INSERT',
CONCAT('NEW => grade_id: ', NEW.grade_id, ', enrollment_id: ', NEW.enrollment_id, ', usual_score: ', NEW.usual_score,
       ', exam_score: ', NEW.exam_score));

CREATE TRIGGER trg_grade_update
AFTER UPDATE ON grade
FOR EACH ROW
INSERT INTO table_change_log(table_name, operation_type, changed_data)
VALUES ('grade', 'UPDATE',
CONCAT('OLD => grade_id: ', OLD.grade_id, ', enrollment_id: ', OLD.enrollment_id, ', usual_score: ', OLD.usual_score,
       ', exam_score: ', OLD.exam_score,
       ' || NEW => grade_id: ', NEW.grade_id, ', enrollment_id: ', NEW.enrollment_id, ', usual_score: ', NEW.usual_score,
       ', exam_score: ', NEW.exam_score));

CREATE TRIGGER trg_grade_delete
AFTER DELETE ON grade
FOR EACH ROW
INSERT INTO table_change_log(table_name, operation_type, changed_data)
VALUES ('grade', 'DELETE',
CONCAT('OLD => grade_id: ', OLD.grade_id, ', enrollment_id: ', OLD.enrollment_id, ', usual_score: ', OLD.usual_score,
       ', exam_score: ', OLD.exam_score));


