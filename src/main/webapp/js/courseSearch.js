document.addEventListener('DOMContentLoaded', function () {
    // 获取表单和表格元素
    const searchForm = document.getElementById('searchForm');
    const resultTable = document.getElementById('resultTable').getElementsByTagName('tbody')[0];

    // 加载学院列表
    loadDepartments();

    // 监听表单提交
    searchForm.addEventListener('submit', function (e) {
        e.preventDefault();
        searchCourses();
    });

    // 加载学院列表函数
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
            if (value) { // 只添加非空值
                searchParams.append(key, value);
            }
        }

        // 发送搜索请求
        fetch('/courseSearch?' + searchParams.toString())
            .then(response => response.json())
            .then(data => displayResults(data))
            .catch(error => {
                console.error('Error searching courses:', error);
                alert('搜索出错，请稍后重试');
            });
    }

    // 显示搜索结果函数
    function displayResults(courses) {
        // 清空现有结果
        resultTable.innerHTML = '';

        if (courses.length === 0) {
            const row = resultTable.insertRow();
            const cell = row.insertCell();
            cell.colSpan = 10;
            cell.textContent = '没有找到匹配的课程';
            cell.style.textAlign = 'center';
            return;
        }

        // 添加新结果
        courses.forEach(course => {
            const row = resultTable.insertRow();
            row.innerHTML = `
                <td>${course.courseId}</td>
                <td>${course.courseName}</td>
                <td>${course.credit}</td>
                <td>${course.teacherId}</td>
                <td>${course.teacherName}</td>
                <td>${course.position}</td>
                <td>${course.classTime}</td>
                <td>${course.classroom}</td>
                <td>${course.capacity}</td>
                <td>${course.enrolledCount}</td>
            `;
        });
    }
}); 