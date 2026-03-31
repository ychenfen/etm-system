const { chromium } = require('playwright');
(async () => {
  const browser = await chromium.launch({ headless: true });
  const ctx = await browser.newContext({ viewport: { width: 1440, height: 900 } });
  const page = await ctx.newPage();
  const BASE = 'http://localhost:8080';
  const DIR = '/home/besp/Desktop/bishe/screenshots_v2';
  require('fs').mkdirSync(DIR, { recursive: true });

  await page.goto(BASE + '/#/login');
  await page.waitForTimeout(1500);
  await page.screenshot({ path: `${DIR}/login.png` });

  await page.fill('input[placeholder="请输入用户名"]', 'admin');
  await page.fill('input[placeholder="请输入密码"]', 'admin123');
  await page.click('button:has-text("登 录")');
  await page.waitForTimeout(2500);
  await page.screenshot({ path: `${DIR}/dashboard.png`, fullPage: true });

  await page.goto(BASE + '/#/teacher');
  await page.waitForTimeout(1500);
  await page.screenshot({ path: `${DIR}/teacher.png` });

  await page.goto(BASE + '/#/salary');
  await page.waitForTimeout(1500);
  await page.screenshot({ path: `${DIR}/salary.png` });

  await page.goto(BASE + '/#/notice');
  await page.waitForTimeout(1500);
  await page.screenshot({ path: `${DIR}/notice.png` });

  await page.goto(BASE + '/#/user');
  await page.waitForTimeout(1500);
  await page.screenshot({ path: `${DIR}/user.png` });

  await browser.close();
  console.log('截图完成');
})();
