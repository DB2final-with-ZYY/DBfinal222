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

    // 加载课程信息和课表
    function loadCourseSchedule() {
        fetch('/studentSchedule')
            .then(response => response.json())
            .then(data => {
                displayCourseList(data);
                displaySchedule(data);
                updateTotalCredits(data);
            })
            .catch(error => {
                console.error('Error loading schedule:', error);
                alert('加载课表失败，请稍后重试');
            });
    }

    // 显示课程列表
    function displayCourseList(courses) {
        const tbody = document.querySelector('#courseTable tbody');
        tbody.innerHTML = '';

        courses.forEach(course => {
            const row = tbody.insertRow();
            const cells = [
                course.courseId,
                course.courseName,
                course.credit,
                course.teacherId,
                course.teacherName,
                course.position,
                course.classTime,
                course.classroom
            ];

            cells.forEach(cellData => {
                const cell = row.insertCell();
                cell.textContent = cellData || '';
            });
        });
    }

    // 显示课表
    function displaySchedule(courses) {
        const scheduleTable = document.getElementById('scheduleTable');
        // 清空现有课程单元格
        for (let i = 0; i < 4; i++) { // 4个时间段
            for (let j = 2; j <= 6; j++) { // 从第3列开始（前两列是节次和时间）
                const cell = scheduleTable.rows[i + 1].cells[j];
                cell.innerHTML = '';
            }
        }

        // 填充课程信息
        courses.forEach(course => {
            const timeMatch = course.classTime.match(/星期(.)(\d-\d)/);
            if (timeMatch) {
                const weekday = '一二三四五'.indexOf(timeMatch[1]);
                const timeSlot = (parseInt(timeMatch[2].split('-')[0]) - 1) / 2;
                
                if (weekday !== -1 && timeSlot >= 0 && timeSlot < 4) {
                    const cell = scheduleTable.rows[timeSlot + 1].cells[weekday + 2]; // +2因为前面多了时间列
                    cell.innerHTML = `
                        <div class="course-cell">
                            <div class="course-name">${course.courseName}</div>
                            <div class="course-info">${course.teacherName}</div>
                            <div class="course-info">${course.classroom}</div>
                        </div>
                    `;
                }
            }
        });
    }

    // 更新总学分
    function updateTotalCredits(courses) {
        const totalCredits = courses.reduce((sum, course) => sum + course.credit, 0);
        document.getElementById('totalCredits').textContent = totalCredits;
    }
}); 