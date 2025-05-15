document.addEventListener('DOMContentLoaded', function () {
    // 加载教师信息
    loadTeacherInfo();
    // 加载学院列表
    loadDepartments();


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
                document.getElementById('teacherStatus').textContent = data.status;
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

}); 