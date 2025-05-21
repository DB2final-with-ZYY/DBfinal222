document.addEventListener('DOMContentLoaded', function () {
    // 获取表单和表格元素
    const searchForm = document.getElementById('searchForm');
    const resultTable = document.getElementById('resultTable').getElementsByTagName('tbody')[0];

    // 加载管理员信息
    loadAdminInfo();

    // 监听表单提交
    searchForm.addEventListener('submit', function (e) {
        e.preventDefault();
        searchExams();
    });

    // 重置按钮功能
    searchForm.addEventListener('reset', function () {
        setTimeout(searchExams, 0);
    });

    // 初始加载
    searchExams();

    // 绑定新增按钮点击事件
    document.getElementById('addExamBtn').addEventListener('click', function() {
        addNewExam();
    });

    // 加载管理员信息
    function loadAdminInfo() {
        fetch('/adminInfo')
            .then(response => response.json())
            .then(data => {
                if (data.error) {
                    alert(data.error);
                    window.location.href = 'login.html';
                    return;
                }
                document.getElementById('adminId').textContent = data.adminId;
                document.getElementById('adminName').textContent = data.name;
                document.getElementById('adminGender').textContent = data.gender;
                document.getElementById('adminEmail').textContent = data.email;
                document.getElementById('adminDepartment').textContent = data.departmentName;
            })
            .catch(error => {
                console.error('Error loading admin info:', error);
                alert('加载管理员信息失败，请重新登录');
                window.location.href = 'login.html';
            });
    }

    // 搜索考试安排
    function searchExams() {
        const formData = new FormData(searchForm);
        const searchParams = new URLSearchParams();

        // 转换表单数据为URL参数
        for (let [key, value] of formData.entries()) {
            if (value) {
                searchParams.append(key, value);
            }
        }

        // 发送搜索请求
        fetch('/examSearch?' + searchParams.toString())
            .then(response => response.json())
            .then(data => {
                if (!Array.isArray(data)) {
                    if (data.error) {
                        throw new Error(data.error);
                    }
                    throw new Error('返回的数据格式不正确');
                }
                displayResults(data);
            })
            .catch(error => {
                console.error('Error searching exams:', error);
                alert('搜索出错：' + error.message);
            });
    }

    // 显示搜索结果
    function displayResults(exams) {
        resultTable.innerHTML = '';

        if (exams.length === 0) {
            const row = resultTable.insertRow();
            const cell = row.insertCell();
            cell.colSpan = 6;
            cell.textContent = '没有找到匹配的考试安排';
            cell.style.textAlign = 'center';
            return;
        }

        exams.forEach(exam => {
            const row = resultTable.insertRow();
            row.dataset.examId = exam.examId;

            // 课程号
            const courseIdCell = row.insertCell();
            courseIdCell.textContent = exam.courseId;
            courseIdCell.dataset.originalValue = exam.courseId;

            // 课程名称
            const courseNameCell = row.insertCell();
            courseNameCell.textContent = exam.courseName;
            courseNameCell.dataset.originalValue = exam.courseName;

            // 教师号
            const teacherIdCell = row.insertCell();
            teacherIdCell.textContent = exam.teacherId;
            teacherIdCell.dataset.originalValue = exam.teacherId;

            // 教师姓名
            const teacherNameCell = row.insertCell();
            teacherNameCell.textContent = exam.teacherName;
            teacherNameCell.dataset.originalValue = exam.teacherName;

            // 考试时间
            const examTimeCell = row.insertCell();
            if (exam.examTime) {
                examTimeCell.textContent = exam.examTime || '';
            examTimeCell.dataset.originalValue = exam.examTime;
            } else {
                const timeInput = document.createElement('input');
                timeInput.type = 'text';
                timeInput.className = 'edit-input';
                timeInput.placeholder = '选择考试时间';
                examTimeCell.appendChild(timeInput);

                // 初始化日期时间选择器
                flatpickr(timeInput, {
                    enableTime: true,
                    dateFormat: "Y-m-d H:i",
                    locale: "zh",
                    minDate: "today",
                    defaultHour: 9,
                    onChange: function (selectedDates, dateStr) {
                        const startTime = selectedDates[0];
                        const endTime = new Date(startTime.getTime() + 2 * 60 * 60 * 1000);
                        timeInput.value = `${dateStr}-${endTime.getHours().toString().padStart(2, '0')}:${endTime.getMinutes().toString().padStart(2, '0')}`;
                    }
                });
            }

            // 考试地点
            const examPlaceCell = row.insertCell();
            examPlaceCell.textContent = exam.examPlace || '';
            examPlaceCell.dataset.originalValue = exam.examPlace;

            // 操作按钮
            const actionCell = row.insertCell();
            const editButton = document.createElement('button');
            editButton.textContent = '编辑';
            editButton.className = 'edit-btn';
            editButton.onclick = () => toggleEditMode(row);
            actionCell.appendChild(editButton);
        });
    }

    // 切换编辑模式
    function toggleEditMode(row) {
        const editButton = row.querySelector('.edit-btn');
        const isEditing = editButton.textContent === '完成';

        if (isEditing) {
            saveChanges(row);
        } else {
            makeRowEditable(row);
            editButton.textContent = '完成';
        }
    }

    // 使行可编辑
    function makeRowEditable(row) {
        const cells = row.cells;

        // 课程号（只读）
        const courseIdCell = cells[0];
        courseIdCell.innerHTML = courseIdCell.textContent;

        // 课程名称（只读）
        const courseNameCell = cells[1];
        courseNameCell.innerHTML = courseNameCell.textContent;

        // 教师号（只读）
        const teacherIdCell = cells[2];
        teacherIdCell.innerHTML = teacherIdCell.textContent;

        // 教师姓名（只读）
        const teacherNameCell = cells[3];
        teacherNameCell.innerHTML = teacherNameCell.textContent;

        // 考试时间（可编辑）
        const examTimeCell = cells[4];
        const examTimeInput = document.createElement('input');
        examTimeInput.type = 'text';
        examTimeInput.value = examTimeCell.textContent;
        examTimeInput.className = 'edit-input';
        examTimeInput.required = true;
        examTimeInput.placeholder = '如：2024-06-20 09:00';
        examTimeCell.textContent = '';
        examTimeCell.appendChild(examTimeInput);

        // 考试地点（可编辑）
        const examPlaceCell = cells[5];
        const examPlaceInput = document.createElement('input');
        examPlaceInput.type = 'text';
        examPlaceInput.value = examPlaceCell.textContent;
        examPlaceInput.className = 'edit-input';
        examPlaceInput.required = true;
        examPlaceCell.textContent = '';
        examPlaceCell.appendChild(examPlaceInput);
    }

    // 保存更改
    function saveChanges(row) {
        const examId = row.dataset.examId;
        
        const cells = row.cells;

        const updatedData = {
            examId: examId,
            examTime: cells[4].querySelector('input').value,
            examPlace: cells[5].querySelector('input').value
        };

        // 验证必填字段
        if (!updatedData.examTime || !updatedData.examPlace) {
            alert('请填写所有必填字段');
            return;
        }

        // 发送更新请求
        fetch('/examUpdate', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=UTF-8'
            },
            body: JSON.stringify(updatedData)
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                searchExams();
                const editButton = row.querySelector('.edit-btn');
                editButton.textContent = '编辑';
            } else {
                alert('更新失败：' + (data.message || '未知错误'));
            }
        })
        .catch(error => {
            console.error('Error updating exam:', error);
            alert('更新失败，请稍后重试');
        });
    }

    // 添加新的考试安排
    function addNewExam() {
        const row = resultTable.insertRow(0);
        row.dataset.isNew = 'true';

        // 考试ID（自动生成且只读，可选）
        // const idCell = row.insertCell();
        // const idInput = document.createElement('input');
        // idInput.type = 'text';
        // idInput.className = 'edit-input';
        // idInput.value = getNextExamId();
        // idInput.readOnly = true;
        // idCell.appendChild(idInput);

        // 课程号
        const courseIdCell = row.insertCell();
        const courseIdInput = document.createElement('input');
        courseIdInput.type = 'text';
        courseIdInput.className = 'edit-input';
        courseIdInput.required = true;
        courseIdCell.appendChild(courseIdInput);

        // 课程名称
        const courseNameCell = row.insertCell();
        const courseNameInput = document.createElement('input');
        courseNameInput.type = 'text';
        courseNameInput.className = 'edit-input';
        courseNameInput.required = true;
        courseNameCell.appendChild(courseNameInput);

        // 教师号
        const teacherIdCell = row.insertCell();
        const teacherIdInput = document.createElement('input');
        teacherIdInput.type = 'text';
        teacherIdInput.className = 'edit-input';
        teacherIdInput.required = true;
        teacherIdCell.appendChild(teacherIdInput);

        // 教师姓名
        const teacherNameCell = row.insertCell();
        const teacherNameInput = document.createElement('input');
        teacherNameInput.type = 'text';
        teacherNameInput.className = 'edit-input';
        teacherNameInput.required = true;
        teacherNameCell.appendChild(teacherNameInput);

        // 考试时间
        const examTimeCell = row.insertCell();
        const examTimeInput = document.createElement('input');
        examTimeInput.type = 'text';
        examTimeInput.className = 'exam-input';
        examTimeInput.placeholder = '选择考试时间';
        examTimeInput.required = true;
        examTimeCell.appendChild(examTimeInput);

        // 初始化日期时间选择器
        flatpickr(examTimeInput, {
            enableTime: true,
            dateFormat: "Y-m-d H:i",
            locale: "zh",
            minDate: "today",
            defaultHour: 9,
            onChange: function (selectedDates, dateStr) {
                const startTime = selectedDates[0];
                const endTime = new Date(startTime.getTime() + 2 * 60 * 60 * 1000);
                examTimeInput.value = `${dateStr}-${endTime.getHours().toString().padStart(2, '0')}:${endTime.getMinutes().toString().padStart(2, '0')}`;
            }
        });

        // 考试地点
        const examPlaceCell = row.insertCell();
        const examPlaceInput = document.createElement('input');
        examPlaceInput.type = 'text';
        examPlaceInput.className = 'edit-input';
        examPlaceInput.placeholder = '输入考试地点';
        examPlaceInput.required = true;
        examPlaceCell.appendChild(examPlaceInput);

        // 操作按钮
        const actionCell = row.insertCell();
        const saveButton = document.createElement('button');
        saveButton.textContent = '保存';
        saveButton.className = 'submit-btn';
        saveButton.onclick = () => saveNewExam(row);
        actionCell.appendChild(saveButton);
    }

    // 保存新的考试安排
    function saveNewExam(row) {
        const cells = row.cells;
        const newExam = {
            courseId: cells[0].querySelector('input').value,
            courseName: cells[1].querySelector('input').value,
            teacherId: cells[2].querySelector('input').value,
            teacherName: cells[3].querySelector('input').value,
            examTime: cells[4].querySelector('input').value,
            examPlace: cells[5].querySelector('input').value
        };

        // 验证必填字段
        if (!newExam.courseId || !newExam.courseName || !newExam.teacherId ||
            !newExam.examTime || !newExam.examPlace) {
            alert('请填写所有必填字段');
            return;
        }

        // 发送新增请求到服务器
        fetch('/addExam', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(newExam)
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('新增考试安排成功');
                searchExams();
            } else {
                alert('新增失败：' + (data.message || '未知错误'));
            }
        })
        .catch(error => {
            console.error('Error adding exam:', error);
            alert('新增失败，请稍后重试');
        });
    }
});