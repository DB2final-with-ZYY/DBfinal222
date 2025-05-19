document.addEventListener('DOMContentLoaded', function () {
    // 获取表单和表格元素
    const searchForm = document.getElementById('searchForm');
    const resultTable = document.getElementById('resultTable').getElementsByTagName('tbody')[0];

    // 加载管理员信息
    loadAdminInfo();
    // 加载学院列表
    loadDepartments();

    // 监听表单提交
    searchForm.addEventListener('submit', function (e) {
        e.preventDefault();
        searchCourses();
    });

    // 重置按钮功能
    searchForm.addEventListener('reset', function () {
        // 需要等表单内容清空后再查
        setTimeout(searchCourses, 0);
    });

    searchCourses();

    // 绑定新增按钮点击事件
    document.getElementById('addCourseBtn').addEventListener('click', function() {
        addNewCourse();
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

    // 加载学院列表
    function loadDepartments() {
        fetch('/departments')
            .then(response => response.json())
            .then(data => {
                const departmentSelect = document.getElementById('department');
                data.forEach(dept => {
                    const option = document.createElement('option');
                    option.value = dept.departmentId;
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
            cell.colSpan = 7; // 更新列数
            cell.textContent = '没有找到匹配的课程';
            cell.style.textAlign = 'center';
            return;
        }

        courses.forEach(course => {
            const row = resultTable.insertRow();
            row.dataset.courseId = course.courseId; // 存储课程ID用于编辑

            // 课程号单元格（不可编辑）
            const idCell = row.insertCell();
            idCell.textContent = course.courseId;

            // 课程名单元格
            const nameCell = row.insertCell();
            nameCell.textContent = course.courseName;
            nameCell.dataset.originalValue = course.courseName;

            // 学分单元格
            const creditCell = row.insertCell();
            creditCell.textContent = course.credit;
            creditCell.dataset.originalValue = course.credit;

            // 考试权重单元格
            const weightCell = row.insertCell();
            weightCell.textContent = course.examWeight;
            weightCell.dataset.originalValue = course.examWeight;

            // 开课学院单元格
            const departmentCell = row.insertCell();
            departmentCell.textContent = course.departmentName;
            departmentCell.dataset.originalValue = course.departmentName;

            // 状态单元格
            const statusCell = row.insertCell();
            statusCell.textContent = course.status;
            statusCell.dataset.originalValue = course.status;

            // 操作按钮单元格
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
            // 保存更改
            saveChanges(row);
        } else {
            // 进入编辑模式
            makeRowEditable(row);
            editButton.textContent = '完成';
        }
    }

    // 使行可编辑
    function makeRowEditable(row) {
        const cells = row.cells;

        // 课程名
        const nameCell = cells[1];
        const nameInput = document.createElement('input');
        nameInput.type = 'text';
        nameInput.value = nameCell.textContent;
        nameInput.className = 'edit-input';
        nameInput.required = true;
        nameCell.textContent = '';
        nameCell.appendChild(nameInput);

        // 学分
        const creditCell = cells[2];
        const creditInput = document.createElement('input');
        creditInput.type = 'number';
        creditInput.value = creditCell.textContent;
        creditInput.className = 'edit-input';
        creditInput.required = true;
        creditInput.min = 0;
        creditInput.max = 10;
        creditCell.textContent = '';
        creditCell.appendChild(creditInput);

        // 考试权重
        const weightCell = cells[3];
        const weightInput = document.createElement('input');
        weightInput.type = 'number';
        weightInput.value = weightCell.textContent;
        weightInput.className = 'edit-input';
        weightInput.required = true;
        weightInput.min = 0;
        weightInput.max = 1;
        weightInput.step = 0.1;
        weightCell.textContent = '';
        weightCell.appendChild(weightInput);

        // 开课学院
        const departmentCell = cells[4];
        const departmentSelect = document.createElement('select');
        departmentSelect.className = 'edit-select';
        departmentSelect.required = true;
        // 复制现有的学院选项
        const departmentOptions = document.getElementById('department').cloneNode(true);
        departmentSelect.innerHTML = departmentOptions.innerHTML;
        // 根据当前显示的院系名称找到对应的ID
        const currentDepartmentName = departmentCell.textContent;
        const currentDepartmentOption = Array.from(departmentOptions.options).find(opt => opt.textContent === currentDepartmentName);
        if (currentDepartmentOption) {
            departmentSelect.value = currentDepartmentOption.value;
        }
        departmentCell.textContent = '';
        departmentCell.appendChild(departmentSelect);

        // 状态
        const statusCell = cells[5];
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
        const courseId = row.dataset.courseId;
        const cells = row.cells;

        const updatedData = {
            courseId: courseId,
            courseName: cells[1].querySelector('input').value,
            credit: cells[2].querySelector('input').value,
            examWeight: cells[3].querySelector('input').value,
            departmentId: cells[4].querySelector('select').value,
            status: cells[5].querySelector('select').value
        };

        // 验证必填字段
        if (!updatedData.courseName || !updatedData.credit || !updatedData.examWeight ||
            !updatedData.departmentId) {
            alert('请填写所有必填字段（课程名、学分、考试权重、开课学院）');
            return;
        }

        // 验证学分范围
        const credit = parseFloat(updatedData.credit);
        if (isNaN(credit) || credit < 0 || credit > 10) {
            alert('学分必须在0-10之间');
            return;
        }

        // 验证考试权重范围
        const weight = parseFloat(updatedData.examWeight);
        if (isNaN(weight) || weight < 0 || weight > 1) {
            alert('考试权重必须在0-1之间');
            return;
        }

        // 发送更新请求到服务器
        fetch('/updateCourse', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(updatedData)
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    // 更新成功，恢复显示模式
                    searchCourses();
                    const editButton = row.querySelector('.edit-btn');
                    editButton.textContent = '编辑';
                } else {
                    alert('更新失败：' + (data.message || '未知错误'));
                }
            })
            .catch(error => {
                console.error('Error updating course:', error);
                alert('更新失败，请稍后重试');
            });
    }

    // 添加新课程行
    function addNewCourse() {
        const row = resultTable.insertRow(0);
        row.dataset.isNew = 'true';

        // 课程号单元格（自动生成且只读）
        const idCell = row.insertCell();
        const idInput = document.createElement('input');
        idInput.type = 'text';
        idInput.className = 'edit-input';
        idInput.value = getNextCourseId();
        idInput.readOnly = true;
        idCell.appendChild(idInput);

        // 课程名单元格
        const nameCell = row.insertCell();
        const nameInput = document.createElement('input');
        nameInput.type = 'text';
        nameInput.className = 'edit-input';
        nameInput.required = true;
        nameCell.appendChild(nameInput);

        // 学分单元格
        const creditCell = row.insertCell();
        const creditInput = document.createElement('input');
        creditInput.type = 'number';
        creditInput.className = 'edit-input';
        creditInput.required = true;
        creditInput.min = 0;
        creditInput.max = 10;
        creditCell.appendChild(creditInput);

        // 考试权重单元格
        const weightCell = row.insertCell();
        const weightInput = document.createElement('input');
        weightInput.type = 'number';
        weightInput.className = 'edit-input';
        weightInput.required = true;
        weightInput.min = 0;
        weightInput.max = 1;
        weightInput.step = 0.1;
        weightCell.appendChild(weightInput);

        // 开课学院单元格
        const departmentCell = row.insertCell();
        const departmentSelect = document.createElement('select');
        departmentSelect.className = 'edit-select';
        departmentSelect.required = true;
        // 复制现有的学院选项
        const departmentOptions = document.getElementById('department').cloneNode(true);
        departmentSelect.innerHTML = departmentOptions.innerHTML;
        departmentCell.appendChild(departmentSelect);

        // 状态单元格
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

        // 操作按钮单元格
        const actionCell = row.insertCell();
        const saveButton = document.createElement('button');
        saveButton.textContent = '保存';
        saveButton.onclick = () => saveNewCourse(row);
        actionCell.appendChild(saveButton);
    }

    // 保存新课程信息
    function saveNewCourse(row) {
        const cells = row.cells;
        const newCourse = {
            courseId: cells[0].querySelector('input').value,
            courseName: cells[1].querySelector('input').value,
            credit: cells[2].querySelector('input').value,
            examWeight: cells[3].querySelector('input').value,
            departmentId: cells[4].querySelector('select').value,
            status: cells[5].querySelector('select').value
        };

        // 验证必填字段
        if (!newCourse.courseId || !newCourse.courseName || !newCourse.credit ||
            !newCourse.examWeight || !newCourse.departmentId) {
            alert('请填写所有必填字段（课程号、课程名、学分、考试权重、开课学院）');
            return;
        }

        // 验证课程号格式（假设课程号必须是数字）
        if (!/^\d+$/.test(newCourse.courseId)) {
            alert('课程号必须是数字');
            return;
        }

        // 验证学分范围
        const credit = parseFloat(newCourse.credit);
        if (isNaN(credit) || credit < 0 || credit > 10) {
            alert('学分必须在0-10之间');
            return;
        }

        // 验证考试权重范围
        const weight = parseFloat(newCourse.examWeight);
        if (isNaN(weight) || weight < 0 || weight > 1) {
            alert('考试权重必须在0-1之间');
            return;
        }

        // 发送新增请求到服务器
        fetch('/addCourse', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(newCourse)
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('新增课程成功');
                    // 刷新表格
                    searchCourses();
                } else {
                    alert('新增失败：' + (data.message || '未知错误'));
                }
            })
            .catch(error => {
                console.error('Error adding course:', error);
                alert('新增失败，请稍后重试');
            });
    }

    function getNextCourseId() {
        let maxId = 0;
        // 遍历表格所有行，找到最大课程号
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
