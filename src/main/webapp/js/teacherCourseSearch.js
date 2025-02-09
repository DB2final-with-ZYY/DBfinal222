document.addEventListener('DOMContentLoaded', function () {
    // 获取表单和表格元素
    const searchForm = document.getElementById('searchForm');
    const resultTable = document.getElementById('resultTable').getElementsByTagName('tbody')[0];

    // 加载教师信息
    loadTeacherInfo();
    // 加载学院列表
    loadDepartments();

    // 监听表单提交
    searchForm.addEventListener('submit', function (e) {
        e.preventDefault();
        searchCourses();
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
            })
            .catch(error => {
                console.error('Error loading teacher info:', error);
                alert('加载教师信息失败，请重新登录');
                window.location.href = 'login.html';
            });
    }

    // 加载学院列表
    function loadDepartments() {
        fetch('/departments')
            .then(response => response.json())
            .then(data => {
                const departmentSelect = document.getElementById('department');
                data.forEach(dept => {
                    const option = document.createElement('option');
                    option.value = dept.departmentName;
                    option.textContent = dept.departmentName;
                    departmentSelect.appendChild(option);
                });
            })
            .catch(error => console.error('Error loading departments:', error));
    }

    // 搜索课程函数
    function searchCourses() {
        // 获取表单数据
        const formData = new FormData(searchForm);
        const searchParams = new URLSearchParams();

        // 转换表单数据为URL参数
        for (let [key, value] of formData.entries()) {
            if (value) {
                searchParams.append(key, value);
            }
        }

        // 发送搜索请求
        fetch('/courseSearch?' + searchParams.toString())
            .then(response => response.json())
            .then(data => {
                displayResults(data);
            })
            .catch(error => {
                console.error('Error searching courses:', error);
                alert('搜索出错，请稍后重试');
            });
    }

    // 显示搜索结果函数
    function displayResults(courses) {
        resultTable.innerHTML = '';

        if (courses.length === 0) {
            const row = resultTable.insertRow();
            const cell = row.insertCell();
            cell.colSpan = 10;  // 10列
            cell.textContent = '没有找到匹配的课程';
            cell.style.textAlign = 'center';
            return;
        }

        courses.forEach(course => {
            const row = resultTable.insertRow();
            const cells = [
                course.courseId,
                course.courseName,
                course.credit,
                course.teacherId,
                course.teacherName,
                course.position,
                course.classTime,
                course.classroom,
                course.capacity,
                course.enrolledCount || "0"
            ];

            cells.forEach(cellData => {
                const cell = row.insertCell();
                cell.textContent = cellData || '';
            });
        });
    }
}); 