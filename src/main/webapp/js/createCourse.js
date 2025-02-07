document.addEventListener('DOMContentLoaded', function () {
    // 加载教师信息
    loadTeacherInfo();
    // 加载学院列表
    loadDepartments();

    // 监听表单提交
    const createCourseForm = document.getElementById('createCourseForm');
    createCourseForm.addEventListener('submit', function (e) {
        e.preventDefault();
        createCourse();
    });

    // 加载教师信息函数
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
            })
            .catch(error => {
                console.error('Error loading teacher info:', error);
                alert('加载教师信息失败，请重新登录');
                window.location.href = 'login.html';
            });
    }

    // 加载学院列表函数
    function loadDepartments() {
        fetch('/departments')
            .then(response => response.json())
            .then(data => {
                const departmentSelect = document.getElementById('department');
                data.forEach(dept => {
                    const option = document.createElement('option');
                    option.value = dept.departmentId;  // 使用学院ID作为value
                    option.textContent = dept.departmentName;
                    departmentSelect.appendChild(option);
                });
            })
            .catch(error => console.error('Error loading departments:', error));
    }

    // 创建课程函数
    function createCourse() {
        const courseName = document.getElementById('courseName').value;
        const departmentId = document.getElementById('department').value;
        const credit = document.getElementById('credit').value;

        if (!courseName || !departmentId || !credit) {
            alert('请填写所有必填项');
            return;
        }

        const courseData = {
            courseName: courseName,
            departmentId: parseInt(departmentId),
            credit: parseInt(credit)
        };

        fetch('/createCourse', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(courseData)
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('课程创建成功！');
                    createCourseForm.reset();
                } else {
                    alert(data.message || '创建失败，请稍后重试');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('创建失败，请稍后重试');
            });
    }
}); 