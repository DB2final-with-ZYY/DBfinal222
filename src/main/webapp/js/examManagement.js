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
            // 确保scheduleId被正确设置
            if (!exam.scheduleId) {
                console.error('Missing scheduleId for exam:', exam);
                return;
            }
            row.dataset.scheduleId = exam.scheduleId;
            row.dataset.examId = exam.examId; // 设置examId
            console.log('Setting row data:', { scheduleId: exam.scheduleId, examId: exam.examId }); // 调试信息

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
            examTimeCell.textContent = exam.examTime || '';
            examTimeCell.dataset.originalValue = exam.examTime;

            // 考试地点
            const examPlaceCell = row.insertCell();
            examPlaceCell.textContent = exam.examPlace || '';
            examPlaceCell.dataset.originalValue = exam.examPlace;

            // 操作按钮
            const actionCell = row.insertCell();
            const actionButton = document.createElement('button');

            // 判断是否有考试信息
            if (!exam.examTime || !exam.examPlace) {
                actionButton.textContent = '创建考试';
                actionButton.className = 'create-btn';
                actionButton.onclick = () => createExam(row);
            } else {
                actionButton.textContent = '编辑';
                actionButton.className = 'edit-btn';
                actionButton.onclick = () => toggleEditMode(row);
            }
            actionCell.appendChild(actionButton);
        });
    }

    // 切换编辑模式
    function toggleEditMode(row) {
        const editButton = row.querySelector('.edit-btn');
        const isEditing = editButton.textContent === '完成';

        if (isEditing) {
            updateExamInfo(row);
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

        // 初始化flatpickr
        flatpickr(examTimeInput, {
            enableTime: true,
            dateFormat: "Y-m-d H:i",
            locale: "zh",
            minDate: "today",
            defaultHour: 9
        });

        // 考试地点（可编辑）
        const examPlaceCell = cells[5];
        const examPlaceInput = document.createElement('input');
        examPlaceInput.type = 'text';
        examPlaceInput.value = examPlaceCell.textContent;
        examPlaceInput.className = 'edit-input';
        examPlaceInput.required = true;
        examPlaceInput.placeholder = '请输入考试地点';
        examPlaceCell.textContent = '';
        examPlaceCell.appendChild(examPlaceInput);
    }

    // 保存更改
    function updateExamInfo(row) {
        const scheduleId = row.dataset.scheduleId;
        const examId = row.dataset.examId; // 获取examId
        
        const cells = row.cells;

        const updatedData = {
            scheduleId: scheduleId,
            examTime: cells[4].querySelector('input').value,
            examPlace: cells[5].querySelector('input').value
        };

        // 只有在编辑现有考试时才添加examId
        if (examId && examId !== 'undefined') {
            updatedData.examId = parseInt(examId);
        }

        console.log('Updating exam with data:', updatedData); // 调试信息

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
            console.log('Update response:', data); // 调试信息
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


    // 保存新的考试安排
    function saveNewExam(row) {
        const cells = row.cells;
        const scheduleId = row.dataset.scheduleId;
        const newExam = {
            scheduleId: scheduleId,
            examTime: cells[4].querySelector('input').value,
            examPlace: cells[5].querySelector('input').value
        };

        // 验证必填字段
        if (!newExam.scheduleId || !newExam.examTime || !newExam.examPlace) {
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

    // 新增考试
    function createExam(row) {
        const scheduleId = row.dataset.scheduleId;
        console.log('Creating exam for scheduleId:', scheduleId); // 调试信息
        makeRowEditable(row);
        
        // 更改按钮文本和行为
        const actionCell = row.cells[row.cells.length - 1];
        const createButton = actionCell.querySelector('button');
        createButton.textContent = '完成';
        createButton.onclick = () => {
            const cells = row.cells;
            const examTimeInput = cells[4].querySelector('input');
            const examPlaceInput = cells[5].querySelector('input');

            // 验证必填字段
            if (!examTimeInput.value.trim() || !examPlaceInput.value.trim()) {
                alert('请填写所有必填字段');
                return;
            }

            const newExam = {
                scheduleId: scheduleId,
                examTime: examTimeInput.value.trim(),
                examPlace: examPlaceInput.value.trim()
            };

            console.log('Sending exam data:', newExam); // 调试信息

            fetch('/addExam', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(newExam)
            })
            .then(response => {
                console.log('Response status:', response.status); // 调试信息
                return response.json();
            })
            .then(data => {
                console.log('Response data:', data); // 调试信息
                if (data.success) {
                    alert('创建考试成功');
                    searchExams();
                } else {
                    alert('创建失败：' + (data.message || '未知错误'));
                }
            })
            .catch(error => {
                console.error('Error creating exam:', error);
                alert('创建失败，请稍后重试');
            });
        };
    }
});