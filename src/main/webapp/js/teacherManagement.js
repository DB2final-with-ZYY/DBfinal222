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
        searchTeachers();
    });

    // 重置按钮功能
    searchForm.addEventListener('reset', function () {
        // 需要等表单内容清空后再查
        setTimeout(searchTeachers, 0);
    });

    searchTeachers();

    // 绑定新增按钮点击事件
    document.getElementById('addTeacherBtn').addEventListener('click', function() {
        addNewTeacher();
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

    // 搜索教师函数
    function searchTeachers() {
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
        fetch('/teacherSearch?' + searchParams.toString())
            .then(response => response.json())
            .then(data => {
                // 确保data是一个数组
                if (!Array.isArray(data)) {
                    if (data.error) {
                        throw new Error(data.error);
                    }
                    throw new Error('返回的数据格式不正确');
                }
                displayResults(data);
            })
            .catch(error => {
                console.error('Error searching teachers:', error);
                alert('搜索出错：' + error.message);
            });
    }

    // 显示搜索结果函数
    function displayResults(teachers) {
        resultTable.innerHTML = '';

        if (teachers.length === 0) {
            const row = resultTable.insertRow();
            const cell = row.insertCell();
            cell.colSpan = 9; // 更新列数
            cell.textContent = '没有找到匹配的教师';
            cell.style.textAlign = 'center';
            return;
        }

        teachers.forEach(teacher => {
            const row = resultTable.insertRow();
            row.dataset.teacherId = teacher.teacherId; // 存储教师ID用于编辑

            // 教师号单元格（不可编辑）
            const idCell = row.insertCell();
            idCell.textContent = teacher.teacherId;

            // 密码单元格
            const passwordCell = row.insertCell();
            passwordCell.textContent = '******'; // 密码显示为星号
            passwordCell.dataset.originalValue = teacher.password;

            // 姓名单元格
            const nameCell = row.insertCell();
            nameCell.textContent = teacher.teacherName;
            nameCell.dataset.originalValue = teacher.teacherName;

            // 性别单元格
            const genderCell = row.insertCell();
            genderCell.textContent = teacher.gender === 'M' ? '男' : (teacher.gender === 'F' ? '女' : '其他');
            genderCell.dataset.originalValue = teacher.gender;

            // 邮箱单元格
            const emailCell = row.insertCell();
            emailCell.textContent = teacher.email;
            emailCell.dataset.originalValue = teacher.email;

            // 职位单元格
            const positionCell = row.insertCell();
            positionCell.textContent = teacher.position;
            positionCell.dataset.originalValue = teacher.position;

            // 学院单元格
            const departmentCell = row.insertCell();
            departmentCell.textContent = teacher.departmentName;
            departmentCell.dataset.originalValue = teacher.departmentName;

            // 状态单元格
            const statusCell = row.insertCell();
            statusCell.textContent = teacher.status;
            statusCell.dataset.originalValue = teacher.status;

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

        // 密码
        const passwordCell = cells[1];
        const passwordInput = document.createElement('input');
        passwordInput.type = 'password';
        passwordInput.value = passwordCell.dataset.originalValue;
        passwordInput.className = 'edit-input';
        passwordInput.required = true;
        passwordCell.textContent = '';
        passwordCell.appendChild(passwordInput);

        // 姓名
        const nameCell = cells[2];
        const nameInput = document.createElement('input');
        nameInput.type = 'text';
        nameInput.value = nameCell.textContent;
        nameInput.className = 'edit-input';
        nameInput.required = true;
        nameCell.textContent = '';
        nameCell.appendChild(nameInput);

        // 性别
        const genderCell = cells[3];
        const genderSelect = document.createElement('select');
        genderSelect.className = 'edit-select';
        ['男', '女', '其他'].forEach(gender => {
            const option = document.createElement('option');
            option.value = gender === '男' ? 'M' : (gender === '女' ? 'F' : 'O');
            option.textContent = gender;
            if (genderCell.textContent === gender) {
                option.selected = true;
            }
            genderSelect.appendChild(option);
        });
        genderCell.textContent = '';
        genderCell.appendChild(genderSelect);

        // 邮箱
        const emailCell = cells[4];
        const emailInput = document.createElement('input');
        emailInput.type = 'email';
        emailInput.value = emailCell.textContent;
        emailInput.className = 'edit-input';
        emailInput.required = true;
        emailCell.textContent = '';
        emailCell.appendChild(emailInput);

        // 职位
        const positionCell = cells[5];
        const positionSelect = document.createElement('select');
        positionSelect.className = 'edit-select';
        ['正教授', '副教授', '高级工程师', '讲师'].forEach(position => {
            const option = document.createElement('option');
            option.value = position;
            option.textContent = position;
            if (positionCell.textContent === position) {
                option.selected = true;
            }
            positionSelect.appendChild(option);
        });
        positionCell.textContent = '';
        positionCell.appendChild(positionSelect);

        // 学院
        const departmentCell = cells[6];
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
        const statusCell = cells[7];
        const statusSelect = document.createElement('select');
        statusSelect.className = 'edit-select';
        ['在职', '离职'].forEach(status => {
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
        const teacherId = row.dataset.teacherId;
        const cells = row.cells;

        const updatedData = {
            teacherId: teacherId,
            password: cells[1].querySelector('input').value,
            name: cells[2].querySelector('input').value,
            gender: cells[3].querySelector('select').value,
            email: cells[4].querySelector('input').value,
            position: cells[5].querySelector('select').value,
            departmentId: cells[6].querySelector('select').value,
            status: cells[7].querySelector('select').value
        };

        // 验证必填字段
        if (!updatedData.password || !updatedData.name || !updatedData.email ||
            !updatedData.departmentId) {
            alert('请填写所有必填字段（密码、姓名、邮箱、学院）');
            return;
        }

        // 发送更新请求到服务器
        fetch('/updateTeacher', {
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
                    searchTeachers();
                    const editButton = row.querySelector('.edit-btn');
                    editButton.textContent = '编辑';
                } else {
                    alert('更新失败：' + (data.message || '未知错误'));
                }
            })
            .catch(error => {
                console.error('Error updating teacher:', error);
                alert('更新失败，请稍后重试');
            });
    }

    // 添加新教师行
    function addNewTeacher() {
        const row = resultTable.insertRow(0);
        row.dataset.isNew = 'true';

        // 教师号单元格（自动生成且只读）
        const idCell = row.insertCell();
        const idInput = document.createElement('input');
        idInput.type = 'text';
        idInput.className = 'edit-input';
        idInput.value = getNextTeacherId();
        idInput.readOnly = true;
        idCell.appendChild(idInput);

        // 密码单元格
        const passwordCell = row.insertCell();
        const passwordInput = document.createElement('input');
        passwordInput.type = 'password';
        passwordInput.className = 'edit-input';
        passwordInput.placeholder = '请输入密码';
        passwordInput.required = true;
        passwordCell.appendChild(passwordInput);

        // 姓名单元格
        const nameCell = row.insertCell();
        const nameInput = document.createElement('input');
        nameInput.type = 'text';
        nameInput.className = 'edit-input';
        nameInput.required = true;
        nameCell.appendChild(nameInput);

        // 性别单元格
        const genderCell = row.insertCell();
        const genderSelect = document.createElement('select');
        genderSelect.className = 'edit-select';
        ['男', '女', '其他'].forEach(gender => {
            const option = document.createElement('option');
            option.value = gender === '男' ? 'M' : (gender === '女' ? 'F' : 'O');
            option.textContent = gender;
            genderSelect.appendChild(option);
        });
        genderCell.appendChild(genderSelect);

        // 邮箱单元格
        const emailCell = row.insertCell();
        const emailInput = document.createElement('input');
        emailInput.type = 'email';
        emailInput.className = 'edit-input';
        emailInput.required = true;
        emailCell.appendChild(emailInput);

        // 职位单元格
        const positionCell = row.insertCell();
        const positionSelect = document.createElement('select');
        positionSelect.className = 'edit-select';
        ['正教授', '副教授', '高级工程师', '讲师'].forEach(position => {
            const option = document.createElement('option');
            option.value = position;
            option.textContent = position;
            positionSelect.appendChild(option);
        });
        positionCell.appendChild(positionSelect);

        // 学院单元格
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
        ['在职', '离职'].forEach(status => {
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
        saveButton.onclick = () => saveNewTeacher(row);
        actionCell.appendChild(saveButton);
    }

    // 保存新教师信息
    function saveNewTeacher(row) {
        const cells = row.cells;
        const newTeacher = {
            teacherId: cells[0].querySelector('input').value,
            password: cells[1].querySelector('input').value,
            name: cells[2].querySelector('input').value,
            gender: cells[3].querySelector('select').value,
            email: cells[4].querySelector('input').value,
            position: cells[5].querySelector('select').value,
            departmentId: cells[6].querySelector('select').value,
            status: cells[7].querySelector('select').value
        };

        // 验证必填字段
        if (!newTeacher.teacherId || !newTeacher.password || !newTeacher.name ||
            !newTeacher.email || !newTeacher.departmentId) {
            alert('请填写所有必填字段（工号、密码、姓名、邮箱、学院）');
            return;
        }

        // 验证工号格式（假设工号必须是数字）
        if (!/^\d+$/.test(newTeacher.teacherId)) {
            alert('工号必须是数字');
            return;
        }

        // 发送新增请求到服务器
        fetch('/addTeacher', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(newTeacher)
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('新增教师成功');
                    // 刷新表格
                    searchTeachers();
                } else {
                    alert('新增失败：' + (data.message || '未知错误'));
                }
            })
            .catch(error => {
                console.error('Error adding teacher:', error);
                alert('新增失败，请稍后重试');
            });
    }

    function getNextTeacherId() {
        let maxId = 0;
        // 遍历表格所有行，找到最大工号
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
