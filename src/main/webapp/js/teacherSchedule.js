document.addEventListener('DOMContentLoaded', function () {
    loadTeacherInfo();
    loadCourseSchedule();
});

// 加载教师信息
function loadTeacherInfo() {
    fetch('/teacherInfo')
        .then(response => response.json())
        .then(data => {
            if (data.error) {
                alert(data.error);
                window.location.href = 'login.html';
                return;
            }
            document.getElementById('teacherId').textContent = data.teacherId;
            document.getElementById('teacherName').textContent = data.name;
            document.getElementById('teacherGender').textContent = data.gender;
            document.getElementById('teacherPosition').textContent = data.position;
            document.getElementById('teacherEmail').textContent = data.email;
            document.getElementById('teacherDepartment').textContent = data.departmentName;
            document.getElementById('teacherStatus').textContent = data.status;
        })
        .catch(error => {
            console.error('Error loading teacher info:', error);
            alert('加载教师信息失败，请重新登录');
            window.location.href = 'login.html';
        });
}

// 加载课程信息和课表
function loadCourseSchedule() {
    fetch('/teacherCourses')
        .then(response => response.json())
        .then(data => {
            if (data.error) {
                alert(data.error);
                return;
            }
            displayCourseList(data);
            displaySchedule(data);
        })
        .catch(error => {
            console.error('Error loading schedule:', error);
            alert('加载课表失败，请稍后重试');
        });
}

// 显示课程列表
function displayCourseList(courses) {
    const tbody = document.getElementById('courseListBody');
    tbody.innerHTML = '';

    if (!courses || courses.length === 0) {
        const row = tbody.insertRow();
        const cell = row.insertCell();
        cell.colSpan = 6;
        cell.textContent = '暂无课程安排';
        cell.style.textAlign = 'center';
        return;
    }

    courses.forEach(course => {
        const row = tbody.insertRow();
        const cells = [
            course.courseId,
            course.courseName,
            course.credit,
            course.classTime,
            course.classroom,
            course.capacity
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
    for (let i = 0; i < 4; i++) {
        for (let j = 2; j <= 6; j++) {
            const cell = scheduleTable.rows[i + 1].cells[j];
            cell.innerHTML = '';
        }
    }

    if (!courses || courses.length === 0) {
        return;
    }

    // 填充课程信息
    courses.forEach(course => {
        const timeMatch = course.classTime.match(/星期(.)(\d-\d)/);
        if (timeMatch) {
            const weekday = '一二三四五'.indexOf(timeMatch[1]);
            const timeSlot = (parseInt(timeMatch[2].split('-')[0]) - 1) / 2;

            if (weekday !== -1 && timeSlot >= 0 && timeSlot < 4) {
                const cell = scheduleTable.rows[timeSlot + 1].cells[weekday + 2];
                // 添加不同颜色的背景
                const randomColor = `hsl(${Math.random() * 360}, 70%, 90%)`;
                cell.innerHTML = `
                        <div class="course-cell" style="background-color: ${randomColor}">
                            <div class="course-name">${course.courseName}</div>
                            <div class="course-info">${course.classroom}</div>
                        </div>
                    `;
            }
        }
    });
}

// // 格式化课程时间
// function formatScheduleTime(dayOfWeek, timeSlot) {
//     const days = ['一', '二', '三', '四', '五'];
//     const timeRanges = {
//         1: "08:00-08:45",
//         2: "08:55-09:40",
//         3: "10:00-10:45",
//         4: "10:55-11:40",
//         5: "13:00-13:45",
//         6: "13:55-14:40",
//         7: "15:00-15:45",
//         8: "15:55-16:40"
//     };
//     return `周${days[dayOfWeek - 1]} ${timeRanges[timeSlot]}`;
// }