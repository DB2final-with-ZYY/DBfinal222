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
        searchStudents();
    });

    // 重置按钮功能
    searchForm.addEventListener('reset', function () {
        // 需要等表单内容清空后再查
        setTimeout(searchStudents, 0);
    });

    searchStudents();

    // 绑定新增按钮点击事件
    document.getElementById('addStudentBtn').addEventListener('click', function() {
        addNewStudent();
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

                // 添加学院选择变化事件监听
                departmentSelect.addEventListener('change', function() {
                    loadMajors(this.value);
                });

                // 初始加载专业列表
                if (departmentSelect.value) {
                    loadMajors(departmentSelect.value);
                }
            })
            .catch(error => console.error('Error loading departments:', error));
    }

    // 加载专业列表
    function loadMajors(departmentId) {
        fetch(`/majors?departmentId=${encodeURIComponent(departmentId)}`)
            .then(response => response.json())
            .then(data => {
                const majorSelect = document.getElementById('major');
                // 清空现有选项，保留"全部"选项
                majorSelect.innerHTML = '<option value="">全部</option>';

                data.forEach(major => {
                    const option = document.createElement('option');
                    option.value = major.majorId;
                    option.textContent = major.majorName;
                    majorSelect.appendChild(option);
                });
            })
            .catch(error => console.error('Error loading majors:', error));
    }

    // 搜索学生函数
    function searchStudents() {
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
        fetch('/studentSearch?' + searchParams.toString())
            .then(response => response.json())
            .then(data => {
                displayResults(data);
            })
            .catch(error => {
                console.error('Error searching students:', error);
                alert('搜索出错，请稍后重试');
            });
    }

    // 显示搜索结果函数
    function displayResults(students) {
        resultTable.innerHTML = '';

        if (students.length === 0) {
            const row = resultTable.insertRow();
            const cell = row.insertCell();
            cell.colSpan = 11; // 更新列数（包括密码字段）
            cell.textContent = '没有找到匹配的学生';
            cell.style.textAlign = 'center';
            return;
        }

        students.forEach(student => {
            const row = resultTable.insertRow();
            row.dataset.studentId = student.studentId; // 存储学生ID用于编辑

            // 学生号单元格（不可编辑）
            const idCell = row.insertCell();
            idCell.textContent = student.studentId;

            // 密码单元格
            const passwordCell = row.insertCell();
            passwordCell.textContent = '******'; // 密码显示为星号
            passwordCell.dataset.originalValue = student.password;

            // 姓名单元格
            const nameCell = row.insertCell();
            nameCell.textContent = student.studentName;
            nameCell.dataset.originalValue = student.studentName;

            // 性别单元格
            const genderCell = row.insertCell();
            genderCell.textContent = student.gender === 'M' ? '男' : (student.gender === 'F' ? '女' : '其他');
            genderCell.dataset.originalValue = student.gender;

            // 邮箱单元格
            const emailCell = row.insertCell();
            emailCell.textContent = student.email;
            emailCell.dataset.originalValue = student.email;

            // 年级单元格
            const gradeCell = row.insertCell();
            gradeCell.textContent = student.gradeNumber;
            gradeCell.dataset.originalValue = student.gradeNumber;

            // 生源地单元格
            const nativePlaceCell = row.insertCell();
            nativePlaceCell.textContent = student.nativePlace;
            nativePlaceCell.dataset.originalValue = student.nativePlace;

            // 学院单元格
            const departmentCell = row.insertCell();
            departmentCell.textContent = student.departmentName;
            departmentCell.dataset.originalValue = student.departmentName;

            // 专业单元格
            const majorCell = row.insertCell();
            majorCell.textContent = student.majorName;
            majorCell.dataset.originalValue = student.majorName;

            // 状态单元格
            const statusCell = row.insertCell();
            statusCell.textContent = student.status;
            statusCell.dataset.originalValue = student.status;

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

        // 年级
        const gradeCell = cells[5];
        const gradeInput = document.createElement('input');
        gradeInput.type = 'number';
        gradeInput.value = gradeCell.textContent;
        gradeInput.className = 'edit-input';
        gradeInput.required = true;
        gradeInput.min = 2010;
        gradeInput.max = 2026;
        gradeCell.textContent = '';
        gradeCell.appendChild(gradeInput);

        // 生源地
        const nativePlaceCell = cells[6];
        const nativePlaceInput = document.createElement('input');
        nativePlaceInput.type = 'text';
        nativePlaceInput.value = nativePlaceCell.textContent;
        nativePlaceInput.className = 'edit-input';
        nativePlaceCell.textContent = '';
        nativePlaceCell.appendChild(nativePlaceInput);

        // 学院
        const departmentCell = cells[7];
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

        // 专业
        const majorCell = cells[8];
        const majorSelect = document.createElement('select');
        majorSelect.className = 'edit-select';
        majorSelect.required = true;
        // 初始加载专业列表
        loadMajorsForEdit(departmentSelect.value, majorSelect, majorCell.dataset.majorId);

        // 添加学院选择变化事件
        departmentSelect.addEventListener('change', function() {
            loadMajorsForEdit(this.value, majorSelect, '');
        });

        majorCell.textContent = '';
        majorCell.appendChild(majorSelect);

        // 状态
        const statusCell = cells[9];
        const statusSelect = document.createElement('select');
        statusSelect.className = 'edit-select';
        ['正常', '试读', '休学', '毕业'].forEach(status => {
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

    // 为编辑模式加载专业列表
    function loadMajorsForEdit(departmentId, majorSelect, currentMajorId) {
        fetch(`/majors?departmentId=${encodeURIComponent(departmentId)}`)
            .then(response => response.json())
            .then(data => {
                // 清空现有选项
                majorSelect.innerHTML = '';

                data.forEach(major => {
                    const option = document.createElement('option');
                    option.value = major.majorId;
                    option.textContent = major.majorName;
                    if (major.majorId === currentMajorId) {
                        option.selected = true;
                    }
                    majorSelect.appendChild(option);
                });
            })
            .catch(error => console.error('Error loading majors for edit:', error));
    }

    // 保存更改
    function saveChanges(row) {
        const studentId = row.dataset.studentId;
        const cells = row.cells;

        const updatedData = {
            studentId: studentId,
            password: cells[1].querySelector('input').value,
            name: cells[2].querySelector('input').value,
            gender: cells[3].querySelector('select').value,
            email: cells[4].querySelector('input').value,
            gradeNumber: cells[5].querySelector('input').value,
            nativePlace: cells[6].querySelector('input').value,
            departmentId: cells[7].querySelector('select').value,
            majorId: cells[8].querySelector('select').value,
            status: cells[9].querySelector('select').value,
        };

        // 验证必填字段
        if (!updatedData.password || !updatedData.name || !updatedData.email ||
            !updatedData.gradeNumber || !updatedData.departmentId || !updatedData.majorId) {
            alert('请填写所有必填字段（密码、姓名、邮箱、年级、学院、专业）');
            return;
        }
        /*
                // 验证密码长度
                if (updatedData.password.length < 6 || updatedData.password.length > 30) {
                    alert('密码长度必须在6-30个字符之间');
                    return;
                }
        */
        // 验证年级格式
        const gradeNumber = parseInt(updatedData.gradeNumber);
        if (isNaN(gradeNumber) || gradeNumber < 2010 || gradeNumber > 2026) {
            alert('年级必须是2010-2026之间的数字');
            return;
        }

        // 发送更新请求到服务器
        fetch('/updateStudent', {
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
                    searchStudents();
                    const editButton = row.querySelector('.edit-btn');
                    editButton.textContent = '编辑';
                } else {
                    alert('更新失败：' + (data.message || '未知错误'));
                }
            })
            .catch(error => {
                console.error('Error updating student:', error);
                alert('更新失败，请稍后重试');
            });
    }

    // 添加新学生行
    function addNewStudent() {
        const row = resultTable.insertRow(0);
        row.dataset.isNew = 'true';

        // 学生号单元格（自动生成且只读）
        const idCell = row.insertCell();
        const idInput = document.createElement('input');
        idInput.type = 'text';
        idInput.className = 'edit-input';
        idInput.value = getNextStudentId();
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

        // 年级单元格
        const gradeCell = row.insertCell();
        const gradeInput = document.createElement('input');
        gradeInput.type = 'number';
        gradeInput.className = 'edit-input';
        gradeInput.required = true;
        gradeInput.min = 2010;
        gradeInput.max = 2026;
        gradeCell.appendChild(gradeInput);

        // 生源地单元格
        const nativePlaceCell = row.insertCell();
        const nativePlaceInput = document.createElement('input');
        nativePlaceInput.type = 'text';
        nativePlaceInput.className = 'edit-input';
        nativePlaceCell.appendChild(nativePlaceInput);

        // 学院单元格
        const departmentCell = row.insertCell();
        const departmentSelect = document.createElement('select');
        departmentSelect.className = 'edit-select';
        departmentSelect.required = true;
        // 复制现有的学院选项
        const departmentOptions = document.getElementById('department').cloneNode(true);
        departmentSelect.innerHTML = departmentOptions.innerHTML;
        departmentCell.appendChild(departmentSelect);

        // 专业单元格
        const majorCell = row.insertCell();
        const majorSelect = document.createElement('select');
        majorSelect.className = 'edit-select';
        majorSelect.required = true;
        majorCell.appendChild(majorSelect);

        // 状态单元格
        const statusCell = row.insertCell();
        const statusSelect = document.createElement('select');
        statusSelect.className = 'edit-select';
        ['正常', '试读', '休学', '毕业'].forEach(status => {
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
        saveButton.onclick = () => saveNewStudent(row);
        actionCell.appendChild(saveButton);

        // 添加学院选择变化事件
        departmentSelect.addEventListener('change', function() {
            loadMajorsForEdit(this.value, majorSelect, '');
        });

        // 初始加载专业列表
        if (departmentSelect.value) {
            loadMajorsForEdit(departmentSelect.value, majorSelect, '');
        }
    }

    // 保存新学生信息
    function saveNewStudent(row) {
        const cells = row.cells;
        const newStudent = {
            studentId: cells[0].querySelector('input').value,
            password: cells[1].querySelector('input').value,
            name: cells[2].querySelector('input').value,
            gender: cells[3].querySelector('select').value,
            email: cells[4].querySelector('input').value,
            gradeNumber: cells[5].querySelector('input').value,
            nativePlace: cells[6].querySelector('input').value,
            departmentId: cells[7].querySelector('select').value,
            majorId: cells[8].querySelector('select').value,
            status: cells[9].querySelector('select').value
        };

        // 验证必填字段
        if (!newStudent.studentId || !newStudent.password || !newStudent.name ||
            !newStudent.email || !newStudent.gradeNumber || !newStudent.departmentId ||
            !newStudent.majorId) {
            alert('请填写所有必填字段（学号、密码、姓名、邮箱、年级、学院、专业）');
            return;
        }

        // 验证学号格式（假设学号必须是数字）
        if (!/^\d+$/.test(newStudent.studentId)) {
            alert('学号必须是数字');
            return;
        }
        /*
                // 验证密码长度
                if (newStudent.password.length < 6 || newStudent.password.length > 30) {
                    alert('密码长度必须在6-30个字符之间');
                    return;
                }
        */
        // 验证年级格式
        const gradeNumber = parseInt(newStudent.gradeNumber);
        if (isNaN(gradeNumber) || gradeNumber < 2010 || gradeNumber > 2026) {
            alert('年级必须是2010-2026之间的数字');
            return;
        }

        // 发送新增请求到服务器
        fetch('/addStudent', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(newStudent)
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('新增学生成功');
                    // 刷新表格
                    searchStudents();
                } else {
                    alert('新增失败：' + (data.message || '未知错误'));
                }
            })
            .catch(error => {
                console.error('Error adding student:', error);
                alert('新增失败，请稍后重试');
            });
    }

    function getNextStudentId() {
        let maxId = 0;
        // 遍历表格所有行，找到最大学号
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