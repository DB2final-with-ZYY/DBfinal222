document.addEventListener('DOMContentLoaded', function () {
    loadTeacherInfo();
    loadClassList();
    setupClassSelect();
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
            document.getElementById('teacherStatus').textContent = data.status;
        })
        .catch(error => {
            console.error('Error loading teacher info:', error);
            alert('加载教师信息失败，请重新登录');
            window.location.href = 'login.html';
        });
}

// 默认考试占比
let examWeight = 0.6; // 默认值

// 加载班级列表(含考试占比)
function loadClassList() {
    fetch('/teacherCourses')
        .then(response => response.json())
        .then(data => {
            const select = document.getElementById('classSelect');
            select.innerHTML = '<option value="">请选择班级</option>';

            data.forEach(classInfo => {
                const option = document.createElement('option');
                option.value = JSON.stringify({
                    courseId: classInfo.courseId,
                    teacherId: classInfo.teacherId,
                    classTime: classInfo.classTime,
                    examWeight: classInfo.examWeight // 添加考试占比
                });
                option.textContent = `${classInfo.courseName} (${classInfo.classTime})`;
                select.appendChild(option);
            });
        })
        .catch(error => console.error('Error loading classes:', error));
}

// 设置班级选择事件
function setupClassSelect() {
    document.getElementById('classSelect').addEventListener('change', function (e) {
        const selectedValue = e.target.value;
        if (selectedValue) {
            const classInfo = JSON.parse(selectedValue);
            loadStudentList(classInfo);
            document.getElementById('studentListContainer').style.display = 'block';
            document.getElementById('analysisContainer').style.display = 'block';
        } else {
            document.getElementById('studentListContainer').style.display = 'none';
            document.getElementById('analysisContainer').style.display = 'none';
        }
    });
}

// 计算总评
function calculateFinalGrade(student, examWeight) {
    const usual = student.usualScore;
    const exam = student.examScore;

    if (usual === '未录入' || exam === '未录入') {
        return '未完善';
    }

    const finalGrade = usual * (1 - examWeight) + exam * examWeight;
    return finalGrade.toFixed(2); // 保留两位小数
}


// 加载学生列表
function loadStudentList(classInfo) {
    fetch('/classStudents', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(classInfo)
    })
        .then(response => response.json())
        .then(data => {
            displayStudentList(data);
            updateClassStatistics(data);
            displayScoreAnalysis(data);
        })
        .catch(error => console.error('Error loading students:', error));
}

// 显示学生列表
function displayStudentList(students) {
    const tbody = document.getElementById('studentListBody');
    tbody.innerHTML = '';

    console.log('Received students data:', students);

    students.forEach(student => {
        console.log('Processing student:', student);

        const row = tbody.insertRow();
        const cells = [
            student.studentId,
            student.studentName,
            student.gender === 'M' ? '男' : '女',
            student.gradeNumber,
            student.departmentName,
            student.majorName,
            // student.email,
            // student.grade || '未录入'  // 使用grade属性作为成绩
            student.usualScore || '未录入',
            student.examScore || '未录入',
            // student.grade || '未录入'
        ];

        console.log('Cells to display:', cells);

        cells.forEach((text, index) => {
            const cell = row.insertCell();
            cell.textContent = text || ''; // 处理 undefined 或 null 值
        });


        // 计算并显示总评
        const classInfo = JSON.parse(document.getElementById('classSelect').value);
        const finalGrade = calculateFinalGrade(student, classInfo.examWeight || examWeight);
        const finalGradeCell = row.insertCell();
        finalGradeCell.textContent = finalGrade;

        // 平时成绩操作
        const usualGradeActionCell = row.insertCell();
        const usualGradeButton = document.createElement('button');
        usualGradeButton.textContent = student.usualScore ? '修改成绩' : '录入成绩';
        usualGradeButton.className = 'button-create';
        usualGradeButton.onclick = () => handleGradeEntry(student, 'usual');
        usualGradeActionCell.appendChild(usualGradeButton);

        // 考试成绩操作
        const examGradeActionCell = row.insertCell();
        const examGradeButton = document.createElement('button');
        examGradeButton.textContent = student.examScore ? '修改成绩' : '录入成绩';
        examGradeButton.className = 'button-create';
        examGradeButton.onclick = () => handleGradeEntry(student, 'exam');
        examGradeActionCell.appendChild(examGradeButton);

        // // 添加录入/修改成绩按钮
        // const gradeActionCell = row.insertCell();
        // const gradeButton = document.createElement('button');
        // gradeButton.textContent = student.grade ? '修改成绩' : '录入成绩';
        // gradeButton.className = 'button-create';
        // gradeButton.onclick = () => handleGradeEntry(student);
        // gradeActionCell.appendChild(gradeButton);

        // // 添加删除按钮
        // const actionCell = row.insertCell();
        // const deleteButton = document.createElement('button');
        // deleteButton.textContent = '移除学生';
        // deleteButton.className = 'delete-btn';
        // deleteButton.onclick = () => removeStudent(student.studentId);
        // actionCell.appendChild(deleteButton);
    });
}

// 处理平时/考试成绩录入/修改
function handleGradeEntry(student, gradeType) {
    const gradeLabel = gradeType === 'usual' ? '平时成绩' : '考试成绩';
    const currentGrade = gradeType === 'usual' ? student.usualScore : student.examScore;
    const newGrade = prompt(`请输入 ${student.studentName} 的${gradeLabel}:`, currentGrade || '');

    if (newGrade !== null) {
        const classInfo = JSON.parse(document.getElementById('classSelect').value);

        // 确定接口路径和操作类型
        const isUpdate = currentGrade !== undefined && currentGrade !== '未录入';
        const url = isUpdate
            ? (gradeType === 'usual' ? '/updateUsualGrade' : '/updateExamGrade')
            : (gradeType === 'usual' ? '/insertUsualGrade' : '/insertExamGrade');

        fetch(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                studentId: student.studentId,
                courseId: classInfo.courseId,
                teacherId: classInfo.teacherId,
                classTime: classInfo.classTime,
                grade: newGrade
            })
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert(isUpdate ? `${gradeLabel}更新成功` : `${gradeLabel}录入成功`);
                    loadStudentList(classInfo);
                } else {
                    alert(data.error || '操作失败，请重试');
                }
            })
            .catch(error => {
                console.error(`Error updating/submitting ${gradeLabel}:`, error);
                alert('操作失败，请重试');
            });
    }
}


// // 处理成绩录入/修改
// function handleGradeEntry(student) {
//     const newGrade = prompt(`请输入 ${student.studentName} 的成绩:`, student.grade || '');
//     if (newGrade !== null) {
//         const classInfo = JSON.parse(document.getElementById('classSelect').value);
//
//         // 判断是提交成绩还是修改成绩
//         const url = student.grade ? '/updateGrade' : '/insertGrade'; // 判断接口
//
//         fetch(url, {
//             method: 'POST',
//             headers: { 'Content-Type': 'application/json' },
//             body: JSON.stringify({
//                 studentId: student.studentId,
//                 courseId: classInfo.courseId,
//                 teacherId: classInfo.teacherId,
//                 classTime: classInfo.classTime,
//                 grade: newGrade
//             })
//         })
//             .then(response => response.json())
//             .then(data => {
//                 if (data.success) {
//                     alert(student.grade ? '成绩更新成功' : '成绩录入成功');
//                     loadStudentList(classInfo);
//                 } else {
//                     alert(data.error || '操作失败，请重试');
//                 }
//             })
//             .catch(error => {
//                 console.error('Error updating/submiting grade:', error);
//                 alert('操作失败，请重试');
//             });
//     }
// }


// 更新班级统计信息
function updateClassStatistics(students) {
    const classInfo = JSON.parse(document.getElementById('classSelect').value);
    const scores = students.map(student => {
        const finalGrade = calculateFinalGrade(student, classInfo.examWeight || examWeight);
        return finalGrade !== '未完善' ? parseFloat(finalGrade) : null;
    }).filter(score => score !== null);

    document.getElementById('totalStudents').textContent = students.length;

    const average = scores.length > 0
        ? (scores.reduce((a, b) => a + b, 0) / scores.length).toFixed(2)
        : '暂无';
    document.getElementById('averageScore').textContent = average;
}

// 显示成绩分析图表
function displayScoreAnalysis(students) {
    const classInfo = JSON.parse(document.getElementById('classSelect').value);
    const scores = students.map(student => {
        const finalGrade = calculateFinalGrade(student, classInfo.examWeight || examWeight);
        return finalGrade !== '未完善' ? parseFloat(finalGrade) : null;
    }).filter(score => score !== null);

    // 分数分布图
    const distributionChart = echarts.init(document.getElementById('scoreDistribution'));
    const distributionOption = {
        title: { text: '成绩分布' },
        tooltip: {},
        xAxis: {
            type: 'category',
            data: ['<60', '60-69', '70-79', '80-89', '90-100']
        },
        yAxis: { type: 'value' },
        series: [{
            data: calculateScoreDistribution(scores),
            type: 'bar'
        }]
    };
    distributionChart.setOption(distributionOption);

    // 成绩统计图
    const statisticsChart = echarts.init(document.getElementById('scoreStatistics'));
    const statisticsOption = {
        title: { text: '成绩统计' },
        tooltip: {},
        radar: {
            indicator: [
                { name: '平均分', max: 100 },
                { name: '最高分', max: 100 },
                { name: '最低分', max: 100 },
                { name: '及格率', max: 100 }
            ]
        },
        series: [{
            type: 'radar',
            data: [{
                value: calculateScoreStatistics(scores),
                name: '成绩指标'
            }]
        }]
    };
    statisticsChart.setOption(statisticsOption);
}

// 计算分数分布
function calculateScoreDistribution(scores) {
    const distribution = [0, 0, 0, 0, 0];
    scores.forEach(score => {
        if (score < 60) distribution[0]++;
        else if (score < 70) distribution[1]++;
        else if (score < 80) distribution[2]++;
        else if (score < 90) distribution[3]++;
        else distribution[4]++;
    });
    return distribution;
}

// 计算成绩统计数据
function calculateScoreStatistics(scores) {
    if (scores.length === 0) return [0, 0, 0, 0];

    const average = scores.reduce((a, b) => a + b, 0) / scores.length;
    const max = Math.max(...scores);
    const min = Math.min(...scores);
    const passRate = (scores.filter(s => s >= 60).length / scores.length) * 100;

    return [average.toFixed(2), max, min, passRate.toFixed(2)];
}



// // 更新班级统计信息
// function updateClassStatistics(students) {
//     document.getElementById('totalStudents').textContent = students.length;
//
//     const scores = students.map(s => s.grade).filter(s => s != null);
//     const average = scores.length > 0
//         ? (scores.reduce((a, b) => a + b, 0) / scores.length).toFixed(2)
//         : '暂无';
//     document.getElementById('averageScore').textContent = average;
// }
//
// // 显示成绩分析图表
// function displayScoreAnalysis(students) {
//     const scores = students.map(s => s.grade).filter(s => s != null);
//
//     // 分数分布图
//     const distributionChart = echarts.init(document.getElementById('scoreDistribution'));
//     const distributionOption = {
//         title: { text: '成绩分布' },
//         tooltip: {},
//         xAxis: {
//             type: 'category',
//             data: ['<60', '60-69', '70-79', '80-89', '90-100']
//         },
//         yAxis: { type: 'value' },
//         series: [{
//             data: calculateScoreDistribution(scores),
//             type: 'bar'
//         }]
//     };
//     distributionChart.setOption(distributionOption);
//
//     // 成绩统计图
//     const statisticsChart = echarts.init(document.getElementById('scoreStatistics'));
//     const statisticsOption = {
//         title: { text: '成绩统计' },
//         tooltip: {},
//         radar: {
//             indicator: [
//                 { name: '平均分', max: 100 },
//                 { name: '最高分', max: 100 },
//                 { name: '最低分', max: 100 },
//                 { name: '及格率', max: 100 }
//             ]
//         },
//         series: [{
//             type: 'radar',
//             data: [{
//                 value: calculateScoreStatistics(scores),
//                 name: '成绩指标'
//             }]
//         }]
//     };
//     statisticsChart.setOption(statisticsOption);
// }
//
// // 计算分数分布
// function calculateScoreDistribution(scores) {
//     const distribution = [0, 0, 0, 0, 0];
//     scores.forEach(score => {
//         if (score < 60) distribution[0]++;
//         else if (score < 70) distribution[1]++;
//         else if (score < 80) distribution[2]++;
//         else if (score < 90) distribution[3]++;
//         else distribution[4]++;
//     });
//     return distribution;
// }
//
// // 计算成绩统计数据
// function calculateScoreStatistics(scores) {
//     if (scores.length === 0) return [0, 0, 0, 0];
//
//     const average = scores.reduce((a, b) => a + b, 0) / scores.length;
//     const max = Math.max(...scores);
//     const min = Math.min(...scores);
//     const passRate = (scores.filter(s => s >= 60).length / scores.length) * 100;
//
//     return [average, max, min, passRate];
// }

// 退课功能
function removeStudent(studentId) {
    if (!confirm('确定要让该学生退课吗？')) {
        return;
    }

    const classInfo = JSON.parse(document.getElementById('classSelect').value);
    fetch('/removeStudent', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            studentId: studentId,
            courseId: classInfo.courseId,
            teacherId: classInfo.teacherId,
            classTime: classInfo.classTime
        })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('退课成功');
                loadStudentList(classInfo);
            } else {
                alert(data.error || '退课失败，请重试');
            }
        })
        .catch(error => {
            console.error('Error removing student:', error);
            alert('退课失败，请重试');
        });
}