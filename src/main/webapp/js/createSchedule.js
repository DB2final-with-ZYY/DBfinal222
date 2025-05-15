document.addEventListener('DOMContentLoaded', function() {
    loadTeacherInfo();
    loadDepartmentCourses();
    setupFormSubmission();
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

// 加载学院课程列表
function loadDepartmentCourses() {
    fetch('/departmentCourses')
        .then(response => response.json())
        .then(data => {
            if (data.error) {
                alert(data.error);
                return;
            }
            displayCourseList(data);
        })
        .catch(error => {
            console.error('Error loading courses:', error);
            alert('加载课程列表失败，请稍后重试');
        });
}

// 显示课程列表
function displayCourseList(courses) {
    const tbody = document.getElementById('courseListBody');
    tbody.innerHTML = '';

    if (!courses || courses.length === 0) {
        const row = tbody.insertRow();
        const cell = row.insertCell();
        cell.colSpan = 4;
        cell.textContent = '暂无可安排课程';
        cell.style.textAlign = 'center';
        return;
    }

    courses.forEach(course => {
        const row = tbody.insertRow();
        
        // 添加课程信息
        ['courseId', 'courseName', 'credit'].forEach(field => {
            const cell = row.insertCell();
            cell.textContent = course[field];
        });

        // 添加操作按钮
        const actionCell = row.insertCell();
        const createButton = document.createElement('button');
        createButton.textContent = '创建安排';
        createButton.className = 'button-create';
        createButton.onclick = () => fillScheduleForm(course);
        actionCell.appendChild(createButton);
    });
}

// 填充课程安排表单
function fillScheduleForm(course) {
    document.getElementById('courseId').value = course.courseId;
    document.getElementById('courseName').value = course.courseName;
    document.getElementById('credit').value = course.credit;
}

// 设置表单提交
function setupFormSubmission() {
    const form = document.getElementById('scheduleForm');
    form.onsubmit = function(e) {
        e.preventDefault();
        
        // 构建课程时间字符串
        const dayOfWeek = document.getElementById('dayOfWeek').value;
        const timeSlot = document.getElementById('timeSlot').value;
        const dayMap = {1: '一', 2: '二', 3: '三', 4: '四', 5: '五'};
        const classTime = `星期${dayMap[dayOfWeek]}${timeSlot}`;

        const scheduleData = {
            courseId: document.getElementById('courseId').value,
            classroom: document.getElementById('classroom').value,
            capacity: document.getElementById('capacity').value,
            semester: document.getElementById('semester').value,
            classTime: classTime
        };

        // 发送创建请求
        fetch('/createSchedule', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(scheduleData)
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('课程安排创建成功！');
                form.reset();
                window.location.href = 'teacherSchedule.html'; // 跳转到课程安排界面
            } else {
                alert(data.error || '创建失败，请重试');
            }
        })
        .catch(error => {
            console.error('Error creating schedule:', error);
            alert('创建失败，请重试');
        });
    };
} 