"""
论文图表自动生成脚本
运行：python3 draw_diagrams.py
输出：diagrams/ 目录下的 PNG 文件
生成：架构图、ER图、状态机图、角色权限图、JWT时序图
"""

import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
from matplotlib.patches import FancyBboxPatch, FancyArrowPatch
from matplotlib.lines import Line2D
import os

# 中文字体
plt.rcParams['font.sans-serif'] = ['STHeiti', 'Arial Unicode MS', 'Songti SC', 'DejaVu Sans']
plt.rcParams['axes.unicode_minus'] = False

OUTPUT_DIR = '/Users/yuchenxu/Desktop/于晨旭毕设/diagrams'
os.makedirs(OUTPUT_DIR, exist_ok=True)

def save(name, dpi=150):
    path = os.path.join(OUTPUT_DIR, f'{name}.png')
    plt.savefig(path, dpi=dpi, bbox_inches='tight', facecolor='white', edgecolor='none')
    plt.close()
    print(f'  ✓ {name}.png')
    return path

# ─── 通用箭头样式 ─────────────────────────────────────────────────────────────

def draw_arrow(ax, x1, y1, x2, y2, color='#555', lw=1.5, both=False):
    style = '<->' if both else '->'
    ax.annotate('', xy=(x2, y2), xytext=(x1, y1),
                arrowprops=dict(arrowstyle=style, color=color,
                                lw=lw, connectionstyle='arc3,rad=0'))

def rounded_box(ax, x, y, w, h, fc='#EEF2FF', ec='#4F5BD5',
                lw=1.5, radius=0.15, text='', fontsize=11,
                bold=False, text_color='#1a1a2e', subtext='', subsize=9):
    box = FancyBboxPatch((x, y), w, h,
                         boxstyle=f'round,pad={radius}',
                         linewidth=lw, edgecolor=ec, facecolor=fc, zorder=3)
    ax.add_patch(box)
    if subtext:
        ax.text(x + w/2, y + h*0.62, text, ha='center', va='center',
                fontsize=fontsize, fontweight='bold' if bold else 'normal',
                color=text_color, zorder=4)
        ax.text(x + w/2, y + h*0.28, subtext, ha='center', va='center',
                fontsize=subsize, color='#555', zorder=4)
    else:
        ax.text(x + w/2, y + h/2, text, ha='center', va='center',
                fontsize=fontsize, fontweight='bold' if bold else 'normal',
                color=text_color, zorder=4)

# ═══════════════════════════════════════════════════════════════════════════════
# 图4-1  系统总体架构图
# ═══════════════════════════════════════════════════════════════════════════════

def fig_architecture():
    fig, ax = plt.subplots(figsize=(11, 8.5))
    ax.set_xlim(0, 11)
    ax.set_ylim(0, 9)
    ax.axis('off')
    ax.set_facecolor('#FAFAFA')
    fig.patch.set_facecolor('#FAFAFA')

    # ── 前端层 ──────────────────────────────────────────────────────────────
    fe_box = FancyBboxPatch((0.4, 7.0), 10.2, 1.7,
                            boxstyle='round,pad=0.12', lw=2,
                            edgecolor='#1565C0', facecolor='#E3F2FD', zorder=2)
    ax.add_patch(fe_box)
    ax.text(5.5, 8.38, '前端层  (Browser)', ha='center', va='center',
            fontsize=13, fontweight='bold', color='#1565C0', zorder=4)

    # 前端子组件
    fe_items = [('Vue 2.6', 1.3), ('Element UI', 3.0),
                ('Vuex / Router', 5.0), ('Axios', 7.1), ('ECharts 5', 8.8)]
    for label, cx in fe_items:
        b = FancyBboxPatch((cx - 0.65, 7.1), 1.3, 0.55,
                           boxstyle='round,pad=0.05', lw=1,
                           edgecolor='#1976D2', facecolor='#BBDEFB', zorder=3)
        ax.add_patch(b)
        ax.text(cx, 7.38, label, ha='center', va='center',
                fontsize=8.5, color='#0D47A1', zorder=4)

    # ── 中间箭头 ────────────────────────────────────────────────────────────
    draw_arrow(ax, 5.5, 6.98, 5.5, 6.72, color='#607D8B', lw=2, both=True)
    ax.text(7.5, 6.84, 'HTTP/REST  |  Authorization: Bearer <JWT>', ha='center',
            va='center', fontsize=9, color='#455A64', style='italic')

    # ── Spring Boot 应用层 ─────────────────────────────────────────────────
    sp_box = FancyBboxPatch((0.4, 1.8), 10.2, 4.75,
                            boxstyle='round,pad=0.12', lw=2,
                            edgecolor='#2E7D32', facecolor='#E8F5E9', zorder=2)
    ax.add_patch(sp_box)
    ax.text(5.5, 6.3, 'Spring Boot 2.7.18 应用层  (后端)', ha='center', va='center',
            fontsize=13, fontweight='bold', color='#1B5E20', zorder=4)

    # Spring Boot 内部分层
    layers = [
        (5.8, '#C8E6C9', '#388E3C', '拦截器链',
         'JwtInterceptor（Token 解析 / ThreadLocal）  →  RoleInterceptor（@RequireRole 校验）'),
        (5.05, '#DCEDC8', '#558B2F', 'Controller 层',
         '参数校验  ·  统一响应封装 R<T>  ·  GlobalExceptionHandler'),
        (4.30, '#F9FBE7', '#827717', 'Service 层',
         '业务逻辑  ·  事务管理  ·  ServiceImpl<M,T>  ·  @Lazy 解决循环依赖'),
        (3.55, '#FFF8E1', '#F57F17', 'Mapper 层',
         'MyBatis-Plus BaseMapper  ·  LambdaQueryWrapper  ·  分页插件  ·  @TableLogic'),
        (2.80, '#FFF3E0', '#E65100', '配置层',
         'WebMvcConfig  ·  MybatisPlusConfig  ·  application.yml  ·  schema.sql / data.sql'),
    ]
    for cy, fc, ec, title, detail in layers:
        r = FancyBboxPatch((0.9, cy - 0.28), 9.2, 0.56,
                           boxstyle='round,pad=0.04', lw=1.2,
                           edgecolor=ec, facecolor=fc, zorder=3)
        ax.add_patch(r)
        ax.text(2.2, cy, title, ha='center', va='center',
                fontsize=10, fontweight='bold', color=ec, zorder=4)
        ax.text(6.1, cy, detail, ha='center', va='center',
                fontsize=8.5, color='#424242', zorder=4)

    # ── 下方箭头 ────────────────────────────────────────────────────────────
    draw_arrow(ax, 5.5, 1.78, 5.5, 1.52, color='#607D8B', lw=2, both=True)
    ax.text(7.2, 1.64, 'JDBC  |  H2 FILE MODE / MySQL 8.0', ha='center',
            va='center', fontsize=9, color='#455A64', style='italic')

    # ── 数据层 ──────────────────────────────────────────────────────────────
    db_box = FancyBboxPatch((0.4, 0.3), 10.2, 1.1,
                            boxstyle='round,pad=0.12', lw=2,
                            edgecolor='#BF360C', facecolor='#FBE9E7', zorder=2)
    ax.add_patch(db_box)
    ax.text(5.5, 1.05, '数据持久层', ha='center', va='center',
            fontsize=13, fontweight='bold', color='#BF360C', zorder=4)
    ax.text(5.5, 0.62, 'H2 嵌入式数据库（开发，零配置）  |  MySQL 8.0（生产）  |  10 张核心业务表',
            ha='center', va='center', fontsize=10, color='#6D4C41', zorder=4)

    plt.title('图4-1  系统总体架构图', fontsize=14, fontweight='bold', pad=12, color='#1a1a2e')
    save('fig4-1_architecture')

# ═══════════════════════════════════════════════════════════════════════════════
# 图4-2  数据库 ER 图
# ═══════════════════════════════════════════════════════════════════════════════

def draw_er_table(ax, x, y, w, name, fields, header_color, border_color, field_h=0.32):
    """画一个ER实体框：标题行 + 字段列表"""
    n = len(fields)
    total_h = 0.5 + n * field_h
    # 标题
    hdr = FancyBboxPatch((x, y - 0.5), w, 0.5,
                         boxstyle='square,pad=0', lw=1.5,
                         edgecolor=border_color, facecolor=header_color, zorder=3)
    ax.add_patch(hdr)
    ax.text(x + w/2, y - 0.25, name, ha='center', va='center',
            fontsize=9, fontweight='bold', color='white', zorder=4)
    # 字段行
    body = FancyBboxPatch((x, y - total_h), w, n * field_h,
                          boxstyle='square,pad=0', lw=1.5,
                          edgecolor=border_color, facecolor='#FAFAFA', zorder=3)
    ax.add_patch(body)
    for i, (pk, fname, ftype) in enumerate(fields):
        fy = y - 0.5 - field_h * (i + 0.5)
        prefix = '[PK] ' if pk == 'PK' else ('[FK] ' if pk == 'FK' else '      ')
        ax.text(x + 0.12, fy, f'{prefix}{fname}', ha='left', va='center',
                fontsize=7.5, color='#1a1a2e', zorder=4)
        ax.text(x + w - 0.1, fy, ftype, ha='right', va='center',
                fontsize=7, color='#666', style='italic', zorder=4)
    # 分隔线（字段之间）
    for i in range(1, n):
        ly = y - 0.5 - field_h * i
        ax.plot([x, x + w], [ly, ly], color=border_color, lw=0.4, alpha=0.4, zorder=4)
    return (x + w/2, y - total_h/2 - 0.25)  # center y of whole box

def fig_er():
    fig, ax = plt.subplots(figsize=(17, 13))
    ax.set_xlim(0, 17)
    ax.set_ylim(0, 13)
    ax.axis('off')
    ax.set_facecolor('#F8F9FA')
    fig.patch.set_facecolor('#F8F9FA')

    # ── 各表定义 ────────────────────────────────────────────────────────────
    # (x, y为左上角, name, fields列表[(PK/FK/'', 字段名, 类型)])
    tables = {
        'sys_user': {
            'pos': (0.3, 12.5), 'w': 3.2,
            'hc': '#1565C0', 'bc': '#1976D2',
            'fields': [
                ('PK', 'id', 'BIGINT'),
                ('', 'username', 'VARCHAR(50)'),
                ('', 'password', 'VARCHAR(100)'),
                ('', 'real_name', 'VARCHAR(50)'),
                ('', 'role', 'ADMIN/DEPT/TEACHER'),
                ('FK', 'department_id', 'BIGINT'),
                ('', 'status', 'INT'),
            ]
        },
        'department': {
            'pos': (4.5, 12.5), 'w': 3.0,
            'hc': '#1565C0', 'bc': '#1976D2',
            'fields': [
                ('PK', 'id', 'BIGINT'),
                ('', 'name', 'VARCHAR(100)'),
                ('', 'code', 'VARCHAR(50)'),
                ('', 'contact_person', 'VARCHAR(50)'),
                ('', 'contact_phone', 'VARCHAR(20)'),
                ('', 'status', 'INT'),
            ]
        },
        'teacher': {
            'pos': (3.8, 8.6), 'w': 3.4,
            'hc': '#2E7D32', 'bc': '#388E3C',
            'fields': [
                ('PK', 'id', 'BIGINT'),
                ('FK', 'user_id', 'BIGINT'),
                ('', 'name', 'VARCHAR(50)'),
                ('', 'education/degree', 'VARCHAR(50)'),
                ('', 'title', 'VARCHAR(50)'),
                ('', 'work_unit', 'VARCHAR(200)'),
                ('FK', 'department_id', 'BIGINT'),
                ('', 'hire_status', 'PENDING/ACTIVE...'),
                ('', 'bank_account', 'VARCHAR(50)'),
            ]
        },
        'course': {
            'pos': (0.3, 6.2), 'w': 3.2,
            'hc': '#6A1B9A', 'bc': '#7B1FA2',
            'fields': [
                ('PK', 'id', 'BIGINT'),
                ('', 'name', 'VARCHAR(100)'),
                ('FK', 'teacher_id', 'BIGINT'),
                ('FK', 'department_id', 'BIGINT'),
                ('', 'semester', 'VARCHAR(50)'),
                ('', 'hours/weekly_hours', 'INT'),
                ('', 'status', 'ACTIVE/...'),
            ]
        },
        'attendance': {
            'pos': (0.3, 3.3), 'w': 3.2,
            'hc': '#4A148C', 'bc': '#6A1B9A',
            'fields': [
                ('PK', 'id', 'BIGINT'),
                ('FK', 'teacher_id', 'BIGINT'),
                ('FK', 'course_id', 'BIGINT'),
                ('', 'attendance_date', 'DATE'),
                ('', 'check_in_time', 'TIMESTAMP'),
                ('', 'actual_hours', 'DECIMAL(4,1)'),
                ('', 'status', 'NORMAL/LATE/...'),
            ]
        },
        'salary': {
            'pos': (8.0, 6.2), 'w': 3.2,
            'hc': '#E65100', 'bc': '#F57C00',
            'fields': [
                ('PK', 'id', 'BIGINT'),
                ('FK', 'teacher_id', 'BIGINT'),
                ('', 'month', 'VARCHAR(20)'),
                ('', 'total_hours', 'DECIMAL(6,1)'),
                ('', 'hour_rate', 'DECIMAL(8,2)'),
                ('', 'bonus/deduction', 'DECIMAL(10,2)'),
                ('', 'total_salary', 'DECIMAL(10,2)'),
                ('', 'status', 'PENDING/PAID'),
            ]
        },
        'evaluation': {
            'pos': (8.0, 3.3), 'w': 3.2,
            'hc': '#B71C1C', 'bc': '#D32F2F',
            'fields': [
                ('PK', 'id', 'BIGINT'),
                ('FK', 'teacher_id', 'BIGINT'),
                ('FK', 'course_id', 'BIGINT'),
                ('', 'semester', 'VARCHAR(50)'),
                ('', 'teaching_score', 'DECIMAL(4,1)'),
                ('', 'student_score', 'DECIMAL(4,1)'),
                ('', 'total_score', 'DECIMAL(4,1)'),
            ]
        },
        'approval': {
            'pos': (3.8, 4.8), 'w': 3.4,
            'hc': '#004D40', 'bc': '#00695C',
            'fields': [
                ('PK', 'id', 'BIGINT'),
                ('FK', 'teacher_id', 'BIGINT'),
                ('', 'approval_no', 'VARCHAR(50)'),
                ('', 'current_node', 'college.../finished'),
                ('', 'approval_status', 'PENDING/APPROVED'),
                ('', 'college_status/by', 'VARCHAR/TIMESTAMP'),
                ('', 'hr_salary_status/by', 'VARCHAR/TIMESTAMP'),
                ('', 'hr_director_status/by', 'VARCHAR/TIMESTAMP'),
                ('', 'proposed_salary', 'DECIMAL(10,2)'),
            ]
        },
        'contract': {
            'pos': (3.8, 1.5), 'w': 3.4,
            'hc': '#1A237E', 'bc': '#283593',
            'fields': [
                ('PK', 'id', 'BIGINT'),
                ('FK', 'teacher_id', 'BIGINT'),
                ('FK', 'approval_id', 'BIGINT'),
                ('', 'contract_no', 'VARCHAR(50)'),
                ('', 'salary_standard', 'DECIMAL(10,2)'),
                ('', 'contract_status', 'UNSIGNED/SIGNED'),
                ('', 'teacher_sign_time', 'TIMESTAMP'),
                ('', 'school_sign_time', 'TIMESTAMP'),
            ]
        },
        'notice': {
            'pos': (11.5, 12.5), 'w': 3.0,
            'hc': '#37474F', 'bc': '#455A64',
            'fields': [
                ('PK', 'id', 'BIGINT'),
                ('', 'title', 'VARCHAR(200)'),
                ('', 'content', 'TEXT'),
                ('', 'type', 'SYSTEM/ADMIN/...'),
                ('', 'publisher', 'VARCHAR(50)'),
                ('', 'status', 'INT'),
            ]
        },
    }

    # ── 画所有表 ─────────────────────────────────────────────────────────────
    centers = {}  # table_name → (cx, top_y, bottom_y, left_x, right_x, w)
    for tname, cfg in tables.items():
        x, y = cfg['pos']
        w = cfg['w']
        draw_er_table(ax, x, y, w, tname, cfg['fields'],
                      cfg['hc'], cfg['bc'])
        n = len(cfg['fields'])
        total_h = 0.5 + n * 0.32
        centers[tname] = {
            'left':  x,
            'right': x + w,
            'top':   y,
            'bot':   y - total_h,
            'cx':    x + w/2,
            'cy':    y - total_h/2,
        }

    # ── 关系连线 ─────────────────────────────────────────────────────────────
    def conn(t1, side1, t2, side2, color='#888', label=''):
        c1 = centers[t1]
        c2 = centers[t2]
        x1 = c1[side1]
        y1 = c1['cy']
        x2 = c2[side2]
        y2 = c2['cy']
        ax.annotate('', xy=(x2, y2), xytext=(x1, y1),
                    arrowprops=dict(arrowstyle='->', color=color, lw=1.5,
                                   connectionstyle='arc3,rad=0.05'),
                    zorder=2)
        if label:
            mx, my = (x1+x2)/2, (y1+y2)/2
            ax.text(mx + 0.1, my + 0.12, label, fontsize=7,
                    color=color, ha='center', zorder=5)

    conn('sys_user', 'right', 'department', 'left', '#1976D2', 'dept_id')
    conn('teacher', 'right', 'department', 'bot', '#388E3C', 'dept_id')
    conn('course', 'right', 'teacher', 'left', '#7B1FA2', 'teacher_id')
    conn('attendance', 'right', 'teacher', 'bot', '#7B1FA2', 'teacher_id')
    conn('salary', 'left', 'teacher', 'right', '#F57C00', 'teacher_id')
    conn('evaluation', 'left', 'teacher', 'right', '#D32F2F', 'teacher_id')
    conn('approval', 'left', 'teacher', 'bot', '#00695C', 'teacher_id')
    conn('contract', 'left', 'teacher', 'bot', '#283593', 'teacher_id')
    conn('contract', 'top', 'approval', 'bot', '#283593', 'approval_id')

    # ── 图例 ─────────────────────────────────────────────────────────────────
    legend_items = [
        mpatches.Patch(facecolor='#1565C0', label='认证模块（sys_user/department）'),
        mpatches.Patch(facecolor='#2E7D32', label='教师档案（teacher）'),
        mpatches.Patch(facecolor='#6A1B9A', label='课程考勤（course/attendance）'),
        mpatches.Patch(facecolor='#004D40', label='审批合同（approval/contract）'),
        mpatches.Patch(facecolor='#E65100', label='薪酬评价（salary/evaluation）'),
    ]
    ax.legend(handles=legend_items, loc='lower right', fontsize=8,
              framealpha=0.9, edgecolor='#ccc')

    # 说明
    ax.text(0.2, 0.3, '[PK]=主键  [FK]=外键  所有表均含 deleted / create_time / update_time 公共字段',
            fontsize=8, color='#555', style='italic')

    plt.title('图4-2  数据库 ER 图（核心实体关系）', fontsize=14,
              fontweight='bold', pad=12, color='#1a1a2e')
    save('fig4-2_er')

# ═══════════════════════════════════════════════════════════════════════════════
# 图4-3  三级审批状态机流程图
# ═══════════════════════════════════════════════════════════════════════════════

def fig_statemachine():
    fig, ax = plt.subplots(figsize=(14, 10))
    ax.set_xlim(0, 14)
    ax.set_ylim(0, 10)
    ax.axis('off')
    ax.set_facecolor('#FAFAFA')
    fig.patch.set_facecolor('#FAFAFA')

    # ── 主流程节点 (x中心, y中心) ───────────────────────────────────────────
    MAIN_X = 7.0

    def state_box(cx, cy, text, fc='#E8F5E9', ec='#2E7D32', w=2.8, h=0.62, sub=''):
        x, y = cx - w/2, cy - h/2
        r = FancyBboxPatch((x, y), w, h,
                           boxstyle='round,pad=0.1', lw=1.8,
                           edgecolor=ec, facecolor=fc, zorder=3)
        ax.add_patch(r)
        if sub:
            ax.text(cx, cy + 0.14, text, ha='center', va='center',
                    fontsize=10, fontweight='bold', color=ec, zorder=4)
            ax.text(cx, cy - 0.15, sub, ha='center', va='center',
                    fontsize=8, color='#555', zorder=4)
        else:
            ax.text(cx, cy, text, ha='center', va='center',
                    fontsize=10, fontweight='bold', color=ec, zorder=4)

    def diamond(cx, cy, text, size=0.55, fc='#FFF8E1', ec='#F57F17'):
        diamond_path = plt.Polygon(
            [[cx, cy+size], [cx+size*1.8, cy], [cx, cy-size], [cx-size*1.8, cy]],
            closed=True, linewidth=1.8, edgecolor=ec, facecolor=fc, zorder=3)
        ax.add_patch(diamond_path)
        ax.text(cx, cy, text, ha='center', va='center',
                fontsize=9, fontweight='bold', color=ec, zorder=4)

    def arrow(x1, y1, x2, y2, label='', color='#555', rad=0, lw=1.8):
        ax.annotate('', xy=(x2, y2), xytext=(x1, y1),
                    arrowprops=dict(arrowstyle='->', color=color, lw=lw,
                                   connectionstyle=f'arc3,rad={rad}'), zorder=2)
        if label:
            mx, my = (x1+x2)/2, (y1+y2)/2
            ax.text(mx + (0.15 if x2 > x1 else -0.15), my, label,
                    ha='center', va='center', fontsize=8.5,
                    color=color, fontweight='bold', zorder=5,
                    bbox=dict(boxstyle='round,pad=0.2', fc='white', ec='none', alpha=0.85))

    # ── 开始圆 ───────────────────────────────────────────────────────────────
    start = plt.Circle((MAIN_X, 9.5), 0.28, color='#1565C0', zorder=4)
    ax.add_patch(start)
    ax.text(MAIN_X, 9.5, '开始', ha='center', va='center',
            fontsize=8, color='white', fontweight='bold', zorder=5)

    # ── 提交申请 ─────────────────────────────────────────────────────────────
    state_box(MAIN_X, 8.7, '提交聘用审批申请',
              fc='#E3F2FD', ec='#1565C0', w=3.0,
              sub='生成 APR 编号  |  状态 PENDING')
    arrow(MAIN_X, 9.22, MAIN_X, 9.0, color='#1565C0')

    # ── 节点1: 学院负责人 ────────────────────────────────────────────────────
    arrow(MAIN_X, 8.39, MAIN_X, 7.9, color='#555')
    state_box(MAIN_X, 7.6, '节点①  学院负责人审批',
              fc='#E8F5E9', ec='#2E7D32', w=3.4,
              sub='current_node = college_leader')
    arrow(MAIN_X, 7.29, MAIN_X, 6.85, color='#555')
    diamond(MAIN_X, 6.5, '通过?')

    # 通过 → 下一节点
    arrow(MAIN_X, 5.95, MAIN_X, 5.5, '通过', color='#2E7D32', lw=2)
    # 驳回 → REJECTED
    arrow(MAIN_X + 1.0, 6.5, 10.8, 6.5, '驳回', color='#C62828', lw=2)
    # 撤回 → REVOKED（蓝色，向左）
    ax.annotate('', xy=(3.2, 7.6), xytext=(MAIN_X - 1.4, 7.6),
                arrowprops=dict(arrowstyle='->', color='#1565C0', lw=1.8,
                                connectionstyle='arc3,rad=-0.3'), zorder=2)
    ax.text(4.5, 8.0, '撤回', ha='center', va='center',
            fontsize=8.5, color='#1565C0', fontweight='bold',
            bbox=dict(boxstyle='round,pad=0.2', fc='white', ec='none', alpha=0.85))
    state_box(2.0, 7.6, 'REVOKED\n已撤回',
              fc='#E3F2FD', ec='#1565C0', w=2.0, h=0.55)

    # ── 节点2: 人事处薪酬岗 ──────────────────────────────────────────────────
    state_box(MAIN_X, 5.2, '节点②  人事处薪酬岗审批',
              fc='#E8F5E9', ec='#2E7D32', w=3.4,
              sub='current_node = hr_salary')
    arrow(MAIN_X, 4.89, MAIN_X, 4.45, color='#555')
    diamond(MAIN_X, 4.1, '通过?')

    arrow(MAIN_X, 3.55, MAIN_X, 3.1, '通过', color='#2E7D32', lw=2)
    arrow(MAIN_X + 1.0, 4.1, 10.8, 4.1, '驳回', color='#C62828', lw=2)

    # ── 节点3: 人事处处长 ────────────────────────────────────────────────────
    state_box(MAIN_X, 2.8, '节点③  人事处处长审批',
              fc='#E8F5E9', ec='#2E7D32', w=3.4,
              sub='current_node = hr_director')
    arrow(MAIN_X, 2.49, MAIN_X, 2.05, color='#555')
    diamond(MAIN_X, 1.7, '通过?')

    arrow(MAIN_X, 1.15, MAIN_X, 0.65, '通过', color='#2E7D32', lw=2)
    arrow(MAIN_X + 1.0, 1.7, 10.8, 1.7, '驳回', color='#C62828', lw=2)

    # ── 最终: 审批通过 + 生成合同 ────────────────────────────────────────────
    state_box(MAIN_X, 0.4, '审批通过 → 自动生成聘用合同',
              fc='#F3E5F5', ec='#6A1B9A', w=4.0,
              sub='approval_status=APPROVED  |  ContractService.generateFromApproval()')

    # ── 右侧 REJECTED 状态 ───────────────────────────────────────────────────
    # 三条驳回线汇聚到一个REJECTED框
    for ry in [6.5, 4.1, 1.7]:
        ax.plot([10.8, 11.5], [ry, ry], color='#C62828', lw=1.8, zorder=2)
    ax.plot([11.5, 11.5], [6.5, 1.7], color='#C62828', lw=1.8, zorder=2)
    ax.annotate('', xy=(11.5, 0.75), xytext=(11.5, 1.7),
                arrowprops=dict(arrowstyle='->', color='#C62828', lw=1.8), zorder=2)
    state_box(11.5, 0.4, 'REJECTED\n已驳回',
              fc='#FFEBEE', ec='#C62828', w=2.2, h=0.65)

    # ── 图例 ─────────────────────────────────────────────────────────────────
    legend_items = [
        mpatches.Patch(facecolor='#E8F5E9', edgecolor='#2E7D32', label='审批节点'),
        mpatches.Patch(facecolor='#FFF8E1', edgecolor='#F57F17', label='判断节点'),
        Line2D([0], [0], color='#2E7D32', lw=2, label='通过路径'),
        Line2D([0], [0], color='#C62828', lw=2, label='驳回路径'),
        Line2D([0], [0], color='#1565C0', lw=2, label='撤回路径'),
    ]
    ax.legend(handles=legend_items, loc='upper left', fontsize=9,
              framealpha=0.9, edgecolor='#ccc')

    plt.title('图4-3  三级聘用审批状态机流程图', fontsize=14,
              fontweight='bold', pad=12, color='#1a1a2e')
    save('fig4-3_statemachine')

# ═══════════════════════════════════════════════════════════════════════════════
# 图3-1  用户角色权限对比
# ═══════════════════════════════════════════════════════════════════════════════

def fig_roles():
    fig, ax = plt.subplots(figsize=(12, 8))
    ax.set_xlim(0, 12)
    ax.set_ylim(0, 8)
    ax.axis('off')
    ax.set_facecolor('#F8F9FA')
    fig.patch.set_facecolor('#F8F9FA')

    roles = [
        ('系统管理员\nADMIN', '#1565C0', '#E3F2FD', [
            '+ 用户管理（增删改查）',
            '+ 院系管理',
            '+ 教师档案管理（全校）',
            '+ 课程管理（全校）',
            '+ 考勤管理（全校）',
            '+ 薪酬管理（全校）',
            '+ 教学评价（全校）',
            '+ 聘用审批（全流程）',
            '+ 合同管理（学校盖章）',
            '+ 首页统计仪表盘',
            '+ 通知公告管理',
            '+ 数据导出（Excel）',
        ]),
        ('学院管理员\nDEPARTMENT', '#2E7D32', '#E8F5E9', [
            '+ 本院教师档案管理',
            '+ 课程管理（本院）',
            '+ 考勤管理（本院）',
            '+ 薪酬管理（本院）',
            '+ 教学评价（本院）',
            '+ 发起聘用审批',
            '+ 合同查看',
            '+ 首页统计仪表盘',
            '+ 通知公告查看',
            '- 用户管理',
            '- 跨院系数据',
            '- 学校盖章操作',
        ]),
        ('外聘教师\nTEACHER', '#E65100', '#FBE9E7', [
            '+ 个人考勤查看',
            '+ 个人薪酬明细',
            '+ 个人评价记录',
            '+ 课程信息查看',
            '+ 通知公告查看',
            '+ 个人信息修改',
            '- 他人数据',
            '- 管理类操作',
            '- 审批流程',
            '- 薪酬录入',
            '- 院系管理',
            '- 用户管理',
        ]),
    ]

    col_w = 3.6
    col_gap = 0.3
    start_x = 0.3
    top_y = 7.5

    for i, (title, ec, fc, perms) in enumerate(roles):
        cx = start_x + i * (col_w + col_gap)
        # 标题
        hdr = FancyBboxPatch((cx, 6.7), col_w, 0.75,
                             boxstyle='round,pad=0.08', lw=2,
                             edgecolor=ec, facecolor=ec, zorder=3)
        ax.add_patch(hdr)
        ax.text(cx + col_w/2, 7.1, title, ha='center', va='center',
                fontsize=11, fontweight='bold', color='white', zorder=4)
        # 权限列表框
        body = FancyBboxPatch((cx, 0.3), col_w, 6.3,
                              boxstyle='round,pad=0.08', lw=1.5,
                              edgecolor=ec, facecolor=fc, zorder=2)
        ax.add_patch(body)
        for j, perm in enumerate(perms):
            py = 6.4 - j * 0.49
            color = '#1B5E20' if perm.startswith('+ ') else '#B71C1C'
            ax.text(cx + 0.25, py, perm, ha='left', va='center',
                    fontsize=9, color=color, zorder=4)
            if j < len(perms) - 1:
                ax.plot([cx + 0.15, cx + col_w - 0.15],
                        [py - 0.24, py - 0.24],
                        color=ec, lw=0.4, alpha=0.35, zorder=3)

    plt.title('图3-1  系统用户角色权限对比', fontsize=14,
              fontweight='bold', pad=12, color='#1a1a2e')
    save('fig3-1_roles')

# ═══════════════════════════════════════════════════════════════════════════════
# 图5-1  JWT 认证时序图
# ═══════════════════════════════════════════════════════════════════════════════

def fig_jwt_sequence():
    fig, ax = plt.subplots(figsize=(13, 9))
    ax.set_xlim(0, 13)
    ax.set_ylim(0, 9)
    ax.axis('off')
    ax.set_facecolor('#FAFAFA')
    fig.patch.set_facecolor('#FAFAFA')

    # ── 角色定义 ─────────────────────────────────────────────────────────────
    actors = [
        (1.5,  '浏览器\n前端',     '#E3F2FD', '#1565C0'),
        (4.5,  'JwtInterceptor\n拦截器',  '#E8F5E9', '#2E7D32'),
        (7.5,  'AuthController\n认证接口', '#FFF3E0', '#E65100'),
        (10.5, 'JwtUtil\n工具类',  '#F3E5F5', '#6A1B9A'),
    ]
    LIFETIME_TOP = 8.3
    LIFETIME_BOT = 0.4

    for ax_x, label, fc, ec in actors:
        # 头部框
        b = FancyBboxPatch((ax_x - 1.0, LIFETIME_TOP), 2.0, 0.55,
                           boxstyle='round,pad=0.06', lw=1.8,
                           edgecolor=ec, facecolor=fc, zorder=3)
        ax.add_patch(b)
        ax.text(ax_x, LIFETIME_TOP + 0.28, label, ha='center', va='center',
                fontsize=9, fontweight='bold', color=ec, zorder=4)
        # 生命线
        ax.plot([ax_x, ax_x], [LIFETIME_BOT, LIFETIME_TOP],
                color=ec, lw=1.2, linestyle='--', alpha=0.5, zorder=1)

    # ── 消息序列 ─────────────────────────────────────────────────────────────
    msgs = [
        # (y, x1, x2, label, color, is_return)
        (7.7,  1.5,  7.5, 'POST /api/auth/login\n{username, password}', '#1565C0', False),
        (6.9,  7.5,  4.5, '调用 UserMapper 查询用户', '#E65100', False),
        (6.2,  4.5,  7.5, '返回 User 对象', '#2E7D32', True),
        (5.5,  7.5, 10.5, 'generateToken(userId, role)', '#E65100', False),
        (4.8, 10.5,  7.5, '返回 JWT 字符串', '#6A1B9A', True),
        (4.1,  7.5,  1.5, '{token: "eyJ..."} → 存入 localStorage', '#E65100', True),
        (3.2,  1.5,  4.5, '后续请求\nAuthorization: Bearer <token>', '#1565C0', False),
        (2.5,  4.5, 10.5, 'parseToken(token)', '#2E7D32', False),
        (1.8, 10.5,  4.5, 'Claims (userId, role)', '#6A1B9A', True),
        (1.1,  4.5,  7.5, '放行 → Controller 处理业务', '#2E7D32', False),
    ]

    for y, x1, x2, label, color, is_return in msgs:
        style = '<-' if is_return else '->'
        ax.annotate('', xy=(x2, y), xytext=(x1, y),
                    arrowprops=dict(arrowstyle=style, color=color, lw=1.6,
                                   connectionstyle='arc3,rad=0'), zorder=3)
        mx = (x1 + x2) / 2
        offset = 0.14 if not is_return else -0.14
        ax.text(mx, y + offset, label, ha='center', va='center',
                fontsize=8.2, color=color, zorder=4,
                bbox=dict(boxstyle='round,pad=0.15', fc='white', ec='none', alpha=0.85))

    # ── 步骤编号 ─────────────────────────────────────────────────────────────
    steps = [(7.7, '①登录'), (6.9, '②查库'), (5.5, '③生成Token'),
             (4.1, '④返回'), (3.2, '⑤带Token请求'), (2.5, '⑥解析'),
             (1.1, '⑦放行')]
    for y, label in steps:
        ax.text(0.15, y, label, ha='left', va='center',
                fontsize=7.5, color='#888', style='italic')

    plt.title('图5-1  JWT 用户认证时序图', fontsize=14,
              fontweight='bold', pad=12, color='#1a1a2e')
    save('fig5-1_jwt_sequence')

# ═══════════════════════════════════════════════════════════════════════════════
# 主流程
# ═══════════════════════════════════════════════════════════════════════════════

print('生成论文图表...')
fig_architecture()
fig_er()
fig_statemachine()
fig_roles()
fig_jwt_sequence()
print(f'\n全部完成，图片保存在: {OUTPUT_DIR}')
