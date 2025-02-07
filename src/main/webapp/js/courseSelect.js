document.addEventListener('DOMContentLoaded', function () {
    // 获取表单和表格元素
    const searchForm = document.getElementById('searchForm');
    const resultTable = document.getElementById('resultTable').getElementsByTagName('tbody')[0];

    // 加载学生信息
    loadStudentInfo();

    // 加载学院列表
    loadDepartments();

    // 加载已选课程
    loadEnrolledCourses();

    // 检查URL参数并自动搜索
    checkUrlParamsAndSearch();

    // 监听表单提交
    searchForm.addEventListener('submit', function (e) {
        e.preventDefault();
        searchCourses();
    });

    // 加载学生信息
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

    // 加载已选课程
    function loadEnrolledCourses() {
        fetch('/enrolled')
            .then(response => response.json())
            .then(data => {
                if (data.error) {
                    alert(data.error);
                    return;
                }
                displayEnrolledCourses(data);
                displaySchedule(data);
                updateTotalCredits(data);
            })
            .catch(error => {
                console.error('Error loading enrolled courses:', error);
                alert('加载已选课程失败，请稍后重试');
            });
    }

    // 检查URL参数并自动搜索
    function checkUrlParamsAndSearch() {
        const urlParams = new URLSearchParams(window.location.search);
        const courseId = urlParams.get('courseId');

        if (courseId) {
            // 设置课程ID输入框的值
            document.getElementById('courseId').value = courseId;
            // 触发搜索
            searchCourses();
        }
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
        // 清空现有结果
        resultTable.innerHTML = '';

        if (courses.length === 0) {
            const row = resultTable.insertRow();
            const cell = row.insertCell();
            cell.colSpan = 11;  // 因为有11列（包括操作列）
            cell.textContent = '没有找到匹配的课程';
            cell.style.textAlign = 'center';
            return;
        }

        // 添加新结果
        courses.forEach(course => {
            const row = resultTable.insertRow();

            // 添加选课按钮
            const actionCell = row.insertCell();
            const selectButton = document.createElement('button');
            selectButton.className = 'select-btn';
            selectButton.textContent = '选课';
            selectButton.onclick = () => selectCourse(course.scheduleId);
            actionCell.appendChild(selectButton);

            // 添加其他课程信息
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
                course.enrolledCount
            ];

            cells.forEach(cellData => {
                const cell = row.insertCell();
                cell.textContent = cellData || '';
            });
        });
    }

    // 选课函数
    function selectCourse(scheduleId) {
        // 从表格行中获取课程信息
        const row = event.target.closest('tr');
        const cells = row.cells;

        // cells[0]是操作按钮列，所以实际数据从cells[1]开始
        const courseData = {
            courseId: parseInt(cells[1].textContent),    // 课程号
            teacherId: parseInt(cells[4].textContent),   // 教师号
            classTime: cells[7].textContent,             // 上课时间
        };

        console.log('选课数据：', courseData); // 添加日志便于调试

        fetch('/selectCourse', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(courseData)
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('选课成功！');
                    searchCourses();     // 刷新搜索结果
                    loadEnrolledCourses(); // 刷新已选课程和课表
                } else {
                    alert(data.message || '选课失败，请稍后重试');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('选课失败，请稍后重试');
            });
    }

    // 退选函数
    function cancelCourse(courseId) {
        fetch('/cancelCourse', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ courseId: courseId })
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('退选成功！');
                    loadStudentInfo(); // 刷新学生信息（包括已选学分）
                    searchCourses(); // 刷新课程列表
                } else {
                    alert(data.message || '退选失败，请稍后重试');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('退选失败，请稍后重试');
            });
    }

    // 显示已选课程列表
    function displayEnrolledCourses(courses) {
        const tbody = document.querySelector('#enrolledTable tbody');
        tbody.innerHTML = '';

        if (!courses || courses.length === 0) {
            const row = tbody.insertRow();
            const cell = row.insertCell();
            cell.colSpan = 8;
            cell.textContent = '暂无已选课程';
            cell.style.textAlign = 'center';
            return;
        }

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
                    const randomColor = `hsl(${Math.random() * 360}, 70%, 90%)`;
                    cell.innerHTML = `
                        <div class="course-cell" style="background-color: ${randomColor}">
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
        const totalCredits = courses ? courses.reduce((sum, course) => sum + (course.credit || 0), 0) : 0;
        document.getElementById('totalCredits').textContent = totalCredits;
    }
}); 