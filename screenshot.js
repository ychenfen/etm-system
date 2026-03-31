const { chromium } = require('playwright');

(async () => {
  const browser = await chromium.launch({ headless: true });
  const context = await browser.newContext({ viewport: { width: 1440, height: 900 } });
  const page = await context.newPage();

  const BASE = 'http://localhost:8080';
  const DIR = '/home/besp/Desktop/bishe/screenshots';

  // 1. 登录页截图
  await page.goto(BASE + '/#/login');
  await page.waitForTimeout(1500);
  await page.screenshot({ path: `${DIR}/01-登录页.png`, fullPage: false });
  console.log('01-登录页 OK');

  // 2. 登录
  await page.fill('input[placeholder="请输入用户名"]', 'admin');
  await page.fill('input[placeholder="请输入密码"]', 'admin123');
  await page.click('button:has-text("登 录")');
  await page.waitForTimeout(2000);

  // 3. Dashboard截图
  await page.waitForTimeout(1500);
  await page.screenshot({ path: `${DIR}/02-首页Dashboard.png`, fullPage: true });
  console.log('02-首页Dashboard OK');

  // 4. 教师管理
  await page.goto(BASE + '/#/teacher');
  await page.waitForTimeout(1500);
  await page.screenshot({ path: `${DIR}/03-教师管理.png`, fullPage: false });
  console.log('03-教师管理 OK');

  // 5. 院系管理
  await page.goto(BASE + '/#/department');
  await page.waitForTimeout(1500);
  await page.screenshot({ path: `${DIR}/04-院系管理.png`, fullPage: false });
  console.log('04-院系管理 OK');

  // 6. 课程管理
  await page.goto(BASE + '/#/course');
  await page.waitForTimeout(1500);
  await page.screenshot({ path: `${DIR}/05-课程管理.png`, fullPage: false });
  console.log('05-课程管理 OK');

  // 7. 考勤管理
  await page.goto(BASE + '/#/attendance');
  await page.waitForTimeout(1500);
  await page.screenshot({ path: `${DIR}/06-考勤管理.png`, fullPage: false });
  console.log('06-考勤管理 OK');

  // 8. 薪酬管理
  await page.goto(BASE + '/#/salary');
  await page.waitForTimeout(1500);
  await page.screenshot({ path: `${DIR}/07-薪酬管理.png`, fullPage: false });
  console.log('07-薪酬管理 OK');

  // 9. 教学评价
  await page.goto(BASE + '/#/evaluation');
  await page.waitForTimeout(1500);
  await page.screenshot({ path: `${DIR}/08-教学评价.png`, fullPage: false });
  console.log('08-教学评价 OK');

  // 10. 通知公告
  await page.goto(BASE + '/#/notice');
  await page.waitForTimeout(1500);
  await page.screenshot({ path: `${DIR}/09-通知公告.png`, fullPage: false });
  console.log('09-通知公告 OK');

  // 11. 用户管理
  await page.goto(BASE + '/#/user');
  await page.waitForTimeout(1500);
  await page.screenshot({ path: `${DIR}/10-用户管理.png`, fullPage: false });
  console.log('10-用户管理 OK');

  // 12. 个人中心
  await page.goto(BASE + '/#/profile');
  await page.waitForTimeout(1500);
  await page.screenshot({ path: `${DIR}/11-个人中心.png`, fullPage: false });
  console.log('11-个人中心 OK');

  // 13. 以教师身份登录截图
  await page.goto(BASE + '/#/login');
  await page.waitForTimeout(500);
  // 先退出
  await page.evaluate(() => { localStorage.clear(); });
  await page.goto(BASE + '/#/login');
  await page.waitForTimeout(1000);
  await page.fill('input[placeholder="请输入用户名"]', 'teacher01');
  await page.fill('input[placeholder="请输入密码"]', 'admin123');
  await page.click('button:has-text("登 录")');
  await page.waitForTimeout(2500);

  // 14. 教师Dashboard
  await page.screenshot({ path: `${DIR}/12-教师首页.png`, fullPage: true });
  console.log('12-教师首页 OK');

  // 15. 我的考勤
  await page.goto(BASE + '/#/my-attendance');
  await page.waitForTimeout(1500);
  await page.screenshot({ path: `${DIR}/13-我的考勤.png`, fullPage: false });
  console.log('13-我的考勤 OK');

  // 16. 我的薪酬
  await page.goto(BASE + '/#/my-salary');
  await page.waitForTimeout(1500);
  await page.screenshot({ path: `${DIR}/14-我的薪酬.png`, fullPage: false });
  console.log('14-我的薪酬 OK');

  // 17. 我的评价
  await page.goto(BASE + '/#/my-evaluation');
  await page.waitForTimeout(1500);
  await page.screenshot({ path: `${DIR}/15-我的评价.png`, fullPage: false });
  console.log('15-我的评价 OK');

  await browser.close();
  console.log('\n全部截图完成！保存在 screenshots/ 目录');
})();
