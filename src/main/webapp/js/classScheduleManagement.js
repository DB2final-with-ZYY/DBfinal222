document.addEventListener('DOMContentLoaded', function () {
    // 获取表单和表格元素
    const searchForm = document.getElementById('searchForm');
    const resultTable = document.getElementById('resultTable').getElementsByTagName('tbody')[0];

    // 加载管理员信息
    loadAdminInfo();

    // 监听表单提交
    searchForm.addEventListener('submit', function (e) {
        e.preventDefault();
        searchSchedules();
    });

    // 重置按钮功能
    searchForm.addEventListener('reset', function () {
        setTimeout(searchSchedules, 0);
    });

    // 初始加载
    searchSchedules();

    // 绑定新增按钮点击事件
    document.getElementById('addTeacherBtn').addEventListener('click', function() {
        addNewSchedule();
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

    // 搜索课程安排
    function searchSchedules() {
        const formData = new FormData(searchForm);
        const searchParams = new URLSearchParams();

        for (let [key, value] of formData.entries()) {
            if (value) {
                searchParams.append(key, value);
            }
        }

        fetch('/scheduleSearch?' + searchParams.toString())
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
                console.error('Error searching schedules:', error);
                alert('搜索出错：' + error.message);
            });
    }

    // 显示搜索结果
    function displayResults(schedules) {
        resultTable.innerHTML = '';

        if (schedules.length === 0) {
            const row = resultTable.insertRow();
            const cell = row.insertCell();
            cell.colSpan = 11;
            cell.textContent = '没有找到匹配的课程安排';
            cell.style.textAlign = 'center';
            return;
        }

        schedules.forEach(schedule => {
            const row = resultTable.insertRow();
            row.dataset.scheduleId = schedule.scheduleId;

            // 安排ID
            const idCell = row.insertCell();
            idCell.textContent = schedule.scheduleId;
            idCell.dataset.originalValue = schedule.scheduleId;

            // 学期
            const semesterCell = row.insertCell();
            semesterCell.textContent = schedule.semester;
            semesterCell.dataset.originalValue = schedule.semester;

            // 课程号
            const courseIdCell = row.insertCell();
            courseIdCell.textContent = schedule.courseId;
            courseIdCell.dataset.originalValue = schedule.courseId;

            // 课程名
            const courseNameCell = row.insertCell();
            courseNameCell.textContent = schedule.courseName;
            courseNameCell.dataset.originalValue = schedule.courseName;

            // 教师号
            const teacherIdCell = row.insertCell();
            teacherIdCell.textContent = schedule.teacherId;
            teacherIdCell.dataset.originalValue = schedule.teacherId;

            // 教师姓名
            const teacherNameCell = row.insertCell();
            teacherNameCell.textContent = schedule.teacherName;
            teacherNameCell.dataset.originalValue = schedule.teacherName;

            // 教室
            const classroomCell = row.insertCell();
            classroomCell.textContent = schedule.classroom;
            classroomCell.dataset.originalValue = schedule.classroom;

            // 容量
            const capacityCell = row.insertCell();
            capacityCell.textContent = schedule.capacity;
            capacityCell.dataset.originalValue = schedule.capacity;

            // 上课时间
            const timeCell = row.insertCell();
            timeCell.textContent = schedule.classTime;
            timeCell.dataset.originalValue = schedule.classTime;

            // 状态
            const statusCell = row.insertCell();
            statusCell.textContent = schedule.status;
            statusCell.dataset.originalValue = schedule.status;

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

        // 学期
        const semesterCell = cells[1];
        const semesterInput = document.createElement('input');
        semesterInput.type = 'text';
        semesterInput.value = semesterCell.textContent;
        semesterInput.className = 'edit-input';
        semesterInput.required = true;
        semesterInput.placeholder = '如：202501';
        semesterCell.textContent = '';
        semesterCell.appendChild(semesterInput);

        // 课程号
        const courseIdCell = cells[2];
        const courseIdInput = document.createElement('input');
        courseIdInput.type = 'text';
        courseIdInput.value = courseIdCell.textContent;
        courseIdInput.className = 'edit-input';
        courseIdInput.required = true;
        courseIdCell.textContent = '';
        courseIdCell.appendChild(courseIdInput);

        // 教师号
        const teacherIdCell = cells[4];
        const teacherIdInput = document.createElement('input');
        teacherIdInput.type = 'text';
        teacherIdInput.value = teacherIdCell.textContent;
        teacherIdInput.className = 'edit-input';
        teacherIdInput.required = true;
        teacherIdCell.textContent = '';
        teacherIdCell.appendChild(teacherIdInput);

        // 教室
        const classroomCell = cells[6];
        const classroomInput = document.createElement('input');
        classroomInput.type = 'text';
        classroomInput.value = classroomCell.textContent;
        classroomInput.className = 'edit-input';
        classroomInput.required = true;
        classroomCell.textContent = '';
        classroomCell.appendChild(classroomInput);

        // 容量
        const capacityCell = cells[7];
        const capacityInput = document.createElement('input');
        capacityInput.type = 'number';
        capacityInput.value = capacityCell.textContent;
        capacityInput.className = 'edit-input';
        capacityInput.required = true;
        capacityCell.textContent = '';
        capacityCell.appendChild(capacityInput);

        // 上课时间
        const timeCell = cells[8];
        const timeInput = document.createElement('input');
        timeInput.type = 'text';
        timeInput.value = timeCell.textContent;
        timeInput.className = 'edit-input';
        timeInput.required = true;
        timeCell.textContent = '';
        timeCell.appendChild(timeInput);

        // 状态
        const statusCell = cells[9];
        const statusSelect = document.createElement('select');
        statusSelect.className = 'edit-select';
        ['待审核', '审核通过', '审核不通过'].forEach(status => {
            const option = document.createElement('option');
            option.value = status;
            option.textContent = status;
            if (statusCell.textContent === status) {
                option.selected = true;
            }
            statusSelect.appendChild(option);
        });
        statusCell.textContent = '';
        statusCell.appendChild(statusSelect);
    }

    // 保存更改
    function saveChanges(row) {
        const scheduleId = row.dataset.scheduleId;
        const cells = row.cells;

        const updatedData = {
            scheduleId: scheduleId,
            semester: cells[1].querySelector('input').value,
            courseId: cells[2].querySelector('input').value,
            teacherId: cells[4].querySelector('input').value,
            classroom: cells[6].querySelector('input').value,
            capacity: cells[7].querySelector('input').value,
            classTime: cells[8].querySelector('input').value,
            status: cells[9].querySelector('select').value
        };

        // 验证必填字段
        if (!updatedData.semester || !updatedData.courseId || !updatedData.teacherId ||
            !updatedData.classroom || !updatedData.capacity || !updatedData.classTime) {
            alert('请填写所有必填字段');
            return;
        }

        // 发送更新请求
        fetch('/updateSchedule', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(updatedData)
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    searchSchedules();
                    const editButton = row.querySelector('.edit-btn');
                    editButton.textContent = '编辑';
                } else {
                    alert('更新失败：' + (data.message || '未知错误'));
                }
            })
            .catch(error => {
                console.error('Error updating schedule:', error);
                alert('更新失败，请稍后重试');
            });
    }

    // 添加新课程安排
    function addNewSchedule() {
        const row = resultTable.insertRow(0);
        row.dataset.isNew = 'true';

        // 安排ID（自动生成且只读）
        const idCell = row.insertCell();
        const idInput = document.createElement('input');
        idInput.type = 'text';
        idInput.className = 'edit-input';
        idInput.value = getNextScheduleId();
        idInput.readOnly = true;
        idCell.appendChild(idInput);

        // 学期
        const semesterCell = row.insertCell();
        const semesterInput = document.createElement('input');
        semesterInput.type = 'text';
        semesterInput.className = 'edit-input';
        semesterInput.placeholder = '如：202501';
        semesterInput.required = true;
        semesterCell.appendChild(semesterInput);

        // 课程号
        const courseIdCell = row.insertCell();
        const courseIdInput = document.createElement('input');
        courseIdInput.type = 'text';
        courseIdInput.className = 'edit-input';
        courseIdInput.required = true;
        courseIdCell.appendChild(courseIdInput);

        // 课程名（只读）
        const courseNameCell = row.insertCell();
        courseNameCell.textContent = '';

        // 教师号
        const teacherIdCell = row.insertCell();
        const teacherIdInput = document.createElement('input');
        teacherIdInput.type = 'text';
        teacherIdInput.className = 'edit-input';
        teacherIdInput.required = true;
        teacherIdCell.appendChild(teacherIdInput);

        // 教师姓名（只读）
        const teacherNameCell = row.insertCell();
        teacherNameCell.textContent = '';

        // 教室
        const classroomCell = row.insertCell();
        const classroomInput = document.createElement('input');
        classroomInput.type = 'text';
        classroomInput.className = 'edit-input';
        classroomInput.required = true;
        classroomCell.appendChild(classroomInput);

        // 容量
        const capacityCell = row.insertCell();
        const capacityInput = document.createElement('input');
        capacityInput.type = 'number';
        capacityInput.className = 'edit-input';
        capacityInput.required = true;
        capacityCell.appendChild(capacityInput);

        // 上课时间
        const timeCell = row.insertCell();
        const timeInput = document.createElement('input');
        timeInput.type = 'text';
        timeInput.className = 'edit-input';
        timeInput.required = true;
        timeCell.appendChild(timeInput);

        // 状态
        const statusCell = row.insertCell();
        const statusSelect = document.createElement('select');
        statusSelect.className = 'edit-select';
        ['待审核', '审核通过', '审核不通过'].forEach(status => {
            const option = document.createElement('option');
            option.value = status;
            option.textContent = status;
            statusSelect.appendChild(option);
        });
        statusCell.appendChild(statusSelect);

        // 操作按钮
        const actionCell = row.insertCell();
        const saveButton = document.createElement('button');
        saveButton.textContent = '保存';
        saveButton.onclick = () => saveNewSchedule(row);
        actionCell.appendChild(saveButton);
    }

    // 保存新课程安排
    function saveNewSchedule(row) {
        const cells = row.cells;
        const newSchedule = {
            scheduleId: cells[0].querySelector('input').value,
            semester: cells[1].querySelector('input').value,
            courseId: cells[2].querySelector('input').value,
            teacherId: cells[4].querySelector('input').value,
            classroom: cells[6].querySelector('input').value,
            capacity: cells[7].querySelector('input').value,
            classTime: cells[8].querySelector('input').value,
            status: cells[9].querySelector('select').value
        };

        // 验证必填字段
        if (!newSchedule.semester || !newSchedule.courseId || !newSchedule.teacherId ||
            !newSchedule.classroom || !newSchedule.capacity || !newSchedule.classTime) {
            alert('请填写所有必填字段');
            return;
        }

        // 发送新增请求
        fetch('/addSchedule', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(newSchedule)
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('新增课程安排成功');
                    searchSchedules();
                } else {
                    alert('新增失败：' + (data.message || '未知错误'));
                }
            })
            .catch(error => {
                console.error('Error adding schedule:', error);
                alert('新增失败，请稍后重试');
            });
    }

    // 获取下一个安排ID
    function getNextScheduleId() {
        let maxId = 0;
        const rows = resultTable.rows;
        for (let i = 0; i < rows.length; i++) {
            const idCell = rows[i].cells[0];
            const id = parseInt(idCell.textContent);
            if (!isNaN(id) && id > maxId) {
                maxId = id;
            }
        }
        return maxId + 1;
    }
});
