document.addEventListener('DOMContentLoaded', function () {
    // 加载学生信息
    loadStudentInfo();
    // 加载课程信息
    loadCourseSchedule();

    // 加载学生信息函数（复用courseSelect.js中的函数）
    function loadStudentInfo() {
        fetch('/studentInfo')
            .then(response => response.json())
            .then(data => {
                if (data.error) {
                    alert(data.error);
                    window.location.href = 'login.html';
                    return;
                }
                document.getElementById('studentId').textContent = data.studentId;
                document.getElementById('studentName').textContent = data.name;
                document.getElementById('studentGender').textContent = data.gender;
                document.getElementById('studentGrade').textContent = data.gradeNumber;
                document.getElementById('studentEmail').textContent = data.email;
                document.getElementById('studentNativePlace').textContent = data.nativePlace;
                document.getElementById('studentDepartment').textContent = data.departmentName;
                document.getElementById('studentMajor').textContent = data.majorName;
                document.getElementById('studentStatus').textContent = data.status;
            })
            .catch(error => {
                console.error('Error loading student info:', error);
                alert('加载学生信息失败，请重新登录');
                window.location.href = 'login.html';
            });
    }

    // 加载课程信息
    function loadCourseSchedule() {
        fetch('/enrolled')
            .then(response => response.json())
            .then(data => {
                if (data.error) {
                    alert(data.error);
                    return;
                }
                // 直接使用返回的数据，因为已经确定是CourseSearchDTO列表
                displayCourseList(data);
                updateTotalCredits(data);
            })
            .catch(error => {
                console.error('Error loading schedule:', error);
                alert('加载失败，请稍后重试');
            });
    }

    // 显示课程列表
    function displayCourseList(DTOs) {
        const tbody = document.querySelector('#courseTable tbody');
        if (!tbody) {
            console.error('未找到课程表的 tbody 元素');
            return;
        }

        tbody.innerHTML = '';

        // 这里修正大小写错误
        if (!DTOs || DTOs.length === 0) {
            const row = tbody.insertRow();
            const cell = row.insertCell();
            cell.colSpan = 9;  // 列数调整为 9
            cell.textContent = '暂无已选课程';
            cell.style.textAlign = 'center';
            return;
        }

        DTOs.forEach(DTO => {
            const row = tbody.insertRow();
            const cells = [
                DTO.courseId || '无',         // 课程号
                DTO.courseName || '无',       // 课程名
                DTO.credit || 0,              // 学分
                DTO.teacherId || '无',        // 教师号
                DTO.teacherName || '无',      // 教师姓名
                DTO.position || '无',         // 教师职称
                DTO.examTime || '未安排',     // 考试时间
                DTO.examPlace || '未安排',    // 考试地点
                DTO.grade ?? '未录入'         // 成绩（可能为 0，所以用 ?? 处理）
            ];

            cells.forEach(cellData => {
                const cell = row.insertCell();
                cell.textContent = cellData;
            });
        });
    }



    // 更新总学分
    function updateTotalCredits(courses) {
        const totalCredits = courses ? courses.reduce((sum, course) => sum + (course.credit || 0), 0) : 0;
        document.getElementById('totalCredits').textContent = totalCredits;
    }
}); 