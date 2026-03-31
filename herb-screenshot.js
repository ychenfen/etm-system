const { chromium } = require('playwright');

(async () => {
  const browser = await chromium.launch({ headless: true });
  const context = await browser.newContext({ viewport: { width: 1440, height: 900 } });
  const page = await context.newPage();

  const BASE = 'http://localhost:5173';
  const DIR = '/home/besp/Desktop/springboot-herb-planting-sales-platform/screenshots';

  // ========== 公共页面 ==========
  // 1. 登录页
  await page.goto(BASE + '/login');
  await page.waitForTimeout(1500);
  await page.screenshot({ path: `${DIR}/01-登录页.png` });
  console.log('01-登录页 OK');

  // 2. 注册页
  await page.goto(BASE + '/register');
  await page.waitForTimeout(1500);
  await page.screenshot({ path: `${DIR}/02-注册页.png` });
  console.log('02-注册页 OK');

  // ========== 管理员端 ==========
  await page.goto(BASE + '/login');
  await page.waitForTimeout(1000);
  await page.fill('input[placeholder*="用户名"]', 'admin');
  await page.fill('input[placeholder*="密码"]', 'admin123');
  await page.click('button:has-text("登")');
  await page.waitForTimeout(3000);

  // 3. 管理员首页
  await page.screenshot({ path: `${DIR}/03-管理员首页.png`, fullPage: true });
  console.log('03-管理员首页 OK');

  // 4-10 系统管理页面
  const adminPages = [
    ['system/user', '04-用户管理'],
    ['system/role', '05-角色管理'],
    ['system/permission', '06-权限管理'],
    ['system/notice', '07-通知管理'],
    ['system/dict', '08-数据字典'],
    ['system/config', '09-系统配置'],
    ['system/log', '10-系统日志'],
  ];
  for (const [path, name] of adminPages) {
    await page.goto(BASE + '/' + path);
    await page.waitForTimeout(1500);
    await page.screenshot({ path: `${DIR}/${name}.png` });
    console.log(name + ' OK');
  }

  // 11-12 分析页面
  const analysisPages = [
    ['analysis/sales', '11-销售分析'],
    ['analysis/yield', '12-产量分析'],
  ];
  for (const [path, name] of analysisPages) {
    await page.goto(BASE + '/' + path);
    await page.waitForTimeout(2000);
    await page.screenshot({ path: `${DIR}/${name}.png` });
    console.log(name + ' OK');
  }

  // 公共功能页面
  const commonPages = [
    ['knowledge/encyclopedia', '13-中药材百科'],
    ['knowledge/disease', '14-病虫害识别'],
    ['knowledge/calendar', '15-种植日历'],
    ['notice', '16-通知中心'],
    ['trace/query', '17-溯源查询'],
  ];
  for (const [path, name] of commonPages) {
    await page.goto(BASE + '/' + path);
    await page.waitForTimeout(1500);
    await page.screenshot({ path: `${DIR}/${name}.png` });
    console.log(name + ' OK');
  }

  // ========== 种植户端 ==========
  // 退出登录，切换账号
  await page.evaluate(() => {
    localStorage.clear();
  });
  await page.goto(BASE + '/login');
  await page.waitForTimeout(1000);
  await page.fill('input[placeholder*="用户名"]', 'farmer001');
  await page.fill('input[placeholder*="密码"]', 'admin123');
  await page.click('button:has-text("登")');
  await page.waitForTimeout(3000);

  // 18. 种植户首页
  await page.screenshot({ path: `${DIR}/18-种植户首页.png`, fullPage: true });
  console.log('18-种植户首页 OK');

  const farmerPages = [
    ['planting/field', '19-地块管理'],
    ['planting/crop', '20-作物管理'],
    ['planting/record', '21-农事记录'],
    ['sales/supply', '22-供应大厅'],
    ['sales/order', '23-订单追踪'],
    ['sales/favorite', '24-我的收藏'],
    ['trace/manage', '25-溯源管理'],
  ];
  for (const [path, name] of farmerPages) {
    await page.goto(BASE + '/' + path);
    await page.waitForTimeout(1500);
    await page.screenshot({ path: `${DIR}/${name}.png` });
    console.log(name + ' OK');
  }

  // ========== 采购商端 ==========
  await page.evaluate(() => { localStorage.clear(); });
  await page.goto(BASE + '/login');
  await page.waitForTimeout(1000);
  await page.fill('input[placeholder*="用户名"]', 'buyer001');
  await page.fill('input[placeholder*="密码"]', 'admin123');
  await page.click('button:has-text("登")');
  await page.waitForTimeout(3000);

  // 26. 采购商首页
  await page.screenshot({ path: `${DIR}/26-采购商首页.png`, fullPage: true });
  console.log('26-采购商首页 OK');

  const buyerPages = [
    ['sales/demand', '27-采购需求'],
    ['sales/supply', '28-采购商-供应大厅'],
    ['sales/order', '29-采购商-订单追踪'],
  ];
  for (const [path, name] of buyerPages) {
    await page.goto(BASE + '/' + path);
    await page.waitForTimeout(1500);
    await page.screenshot({ path: `${DIR}/${name}.png` });
    console.log(name + ' OK');
  }

  await browser.close();
  console.log('\n全部截图完成！保存在 screenshots/ 目录');
})();
