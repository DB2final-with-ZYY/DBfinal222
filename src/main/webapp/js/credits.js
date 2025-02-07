document.addEventListener('DOMContentLoaded', function () {
    // 加载学生信息
    loadStudentInfo();
    // 加载课程计划和已选课程
    loadCoursePlanAndEnrollments();

    // 加载学生信息函数
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

    // 加载课程计划和已选课程
    async function loadCoursePlanAndEnrollments() {
        try {
            // 并行获取课程计划和已选课程
            const [coursePlanResponse, enrollmentsResponse] = await Promise.all([
                fetch('/coursePlan'),
                fetch('/enrolled')
            ]);

            const coursePlanData = await coursePlanResponse.json();
            const enrollmentsData = await enrollmentsResponse.json();

            if (coursePlanData.error) {
                alert(coursePlanData.error);
                return;
            }

            // 创建已选课程的映射，用于快速查找
            const enrolledCourses = new Map();
            enrollmentsData.forEach(enrollment => {
                enrolledCourses.set(enrollment.courseId, enrollment);
            });

            displayCoursePlan(coursePlanData, enrolledCourses);
        } catch (error) {
            console.error('Error loading data:', error);
            alert('加载数据失败，请稍后重试');
        }
    }

    // 显示课程计划
    function displayCoursePlan(courses, enrolledCourses) {
        const tbody = document.querySelector('#creditsTable tbody');
        tbody.innerHTML = '';

        if (!courses || courses.length === 0) {
            const row = tbody.insertRow();
            const cell = row.insertCell();
            cell.colSpan = 6;
            cell.textContent = '暂无课程计划信息';
            cell.style.textAlign = 'center';
            return;
        }

        courses.forEach(course => {
            const row = tbody.insertRow();
            
            // 添加课程基本信息
            [course.courseId, course.courseName, course.credit].forEach(text => {
                const cell = row.insertCell();
                cell.textContent = text || '';
            });

            // 检查课程是否已选
            const enrolledCourse = enrolledCourses.get(course.courseId);
            
            // 添加完成情况
            const statusCell = row.insertCell();
            const gradeCell = row.insertCell();
            
            if (enrolledCourse) {
                // 如果已经选了这门课
                statusCell.textContent = '进行中';
                statusCell.className = 'status-in-progress';
                gradeCell.textContent = '-';
            } else if (course.grade) {
                // 如果有成绩
                statusCell.textContent = '已完成';
                statusCell.className = 'status-completed';
                gradeCell.textContent = course.grade;
            } else {
                // 如果既没选也没完成
                statusCell.textContent = '未完成';
                statusCell.className = 'status-incomplete';
                gradeCell.textContent = '-';
            }

            // 添加操作按钮
            const actionCell = row.insertCell();
            if (!enrolledCourse && !course.grade) {
                // 只有未选且未完成的课程才显示去选课按钮
                const selectButton = document.createElement('button');
                selectButton.className = 'select-course-btn';
                selectButton.textContent = '去选课';
                selectButton.onclick = () => goToSelectCourse(course.courseId);
                actionCell.appendChild(selectButton);
            }
        });
    }

    // 跳转到选课页面并填入课程号
    function goToSelectCourse(courseId) {
        window.location.href = `courseSelect.html?courseId=${courseId}`;
    }
}); 