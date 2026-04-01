const { chromium } = require('playwright');
(async () => {
  const browser = await chromium.launch({ headless: true });
  const ctx = await browser.newContext({ viewport: { width: 1440, height: 900 } });
  const page = await ctx.newPage();
  const B = 'http://localhost:8080';
  const D = '/home/besp/Desktop/bishe/screenshots_v2';

  // 登录
  await page.goto(B + '/#/login');
  await page.waitForTimeout(1000);
  await page.fill('input[placeholder="请输入用户名"]', 'admin');
  await page.fill('input[placeholder="请输入密码"]', 'admin123');
  await page.click('button:has-text("登 录")');
  await page.waitForTimeout(2500);

  // 所有页面截图
  const pages = [
    ['dashboard', '/#/dashboard', false],
    ['teacher', '/#/teacher', false],
    ['department', '/#/department', false],
    ['course', '/#/course', false],
    ['attendance', '/#/attendance', false],
    ['salary', '/#/salary', false],
    ['evaluation', '/#/evaluation', false],
    ['notice', '/#/notice', false],
    ['user', '/#/user', false],
    ['profile', '/#/profile', false],
  ];

  for (const [name, path, full] of pages) {
    await page.goto(B + path);
    await page.waitForTimeout(1500);
    await page.screenshot({ path: `${D}/${name}.png`, fullPage: full === true });
    console.log(name + ' OK');
  }

  // 测试弹窗 - 教师新增对话框
  await page.goto(B + '/#/teacher');
  await page.waitForTimeout(1500);
  const addBtn = await page.$('button:has-text("新增教师")');
  if (addBtn) {
    await addBtn.click();
    await page.waitForTimeout(800);
    await page.screenshot({ path: `${D}/teacher_dialog.png` });
    console.log('teacher_dialog OK');
    // 关闭
    const closeBtn = await page.$('.el-dialog__headerbtn');
    if (closeBtn) await closeBtn.click();
  }

  // 教师端
  await page.evaluate(() => { localStorage.clear(); });
  await page.goto(B + '/#/login');
  await page.waitForTimeout(1000);
  await page.fill('input[placeholder="请输入用户名"]', 'teacher01');
  await page.fill('input[placeholder="请输入密码"]', 'admin123');
  await page.click('button:has-text("登 录")');
  await page.waitForTimeout(2500);
  await page.screenshot({ path: `${D}/teacher_dashboard.png`, fullPage: true });
  console.log('teacher_dashboard OK');

  await page.goto(B + '/#/my-attendance');
  await page.waitForTimeout(1500);
  await page.screenshot({ path: `${D}/my_attendance.png` });
  console.log('my_attendance OK');

  await page.goto(B + '/#/my-salary');
  await page.waitForTimeout(1500);
  await page.screenshot({ path: `${D}/my_salary.png` });
  console.log('my_salary OK');

  await page.goto(B + '/#/my-evaluation');
  await page.waitForTimeout(1500);
  await page.screenshot({ path: `${D}/my_evaluation.png` });
  console.log('my_evaluation OK');

  await browser.close();
  console.log('全部完成');
})();
