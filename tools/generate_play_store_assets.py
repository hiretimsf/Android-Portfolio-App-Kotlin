#!/usr/bin/env python3
from pathlib import Path
import math

from PIL import Image, ImageDraw, ImageFilter, ImageFont


ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "play-store-assets"
PROFILE_IMAGE = ROOT / "app/src/main/res/drawable-nodpi/profile.webp"
APP_ICON_IMAGE = ROOT / "app/src/main/ic_launcher-web.png"

NAVY = "#101B2D"
INK = "#122033"
MUTED = "#657184"
BLUE = "#1967D2"
TEAL = "#0F9D8A"
GREEN = "#34A853"
YELLOW = "#F9AB00"
SURFACE = "#FFFFFF"
SOFT = "#F3F7FB"


def font(size, bold=False):
    candidates = [
        "/System/Library/Fonts/Supplemental/Arial Bold.ttf" if bold else "/System/Library/Fonts/Supplemental/Arial.ttf",
        "/System/Library/Fonts/HelveticaNeue.ttc",
        "/Library/Fonts/Arial.ttf",
    ]
    for candidate in candidates:
        if candidate and Path(candidate).exists():
            return ImageFont.truetype(candidate, size=size)
    return ImageFont.load_default()


def text_size(draw, text, fnt):
    box = draw.textbbox((0, 0), text, font=fnt)
    return box[2] - box[0], box[3] - box[1]


def wrap_text(draw, text, fnt, max_width):
    words = text.split()
    lines = []
    current = ""
    for word in words:
        test = word if not current else f"{current} {word}"
        if text_size(draw, test, fnt)[0] <= max_width:
            current = test
        else:
            if current:
                lines.append(current)
            current = word
    if current:
        lines.append(current)
    return lines


def draw_multiline(draw, xy, text, fnt, fill, max_width, line_gap=10, align="left"):
    x, y = xy
    for line in wrap_text(draw, text, fnt, max_width):
        w, h = text_size(draw, line, fnt)
        dx = {"left": 0, "center": (max_width - w) / 2, "right": max_width - w}[align]
        draw.text((x + dx, y), line, font=fnt, fill=fill)
        y += h + line_gap
    return y


def shadowed_round_rect(base, box, radius, fill, shadow=(15, 25, 50, 45), offset=(0, 18), blur=24, outline=None, width=1):
    layer = Image.new("RGBA", base.size, (0, 0, 0, 0))
    d = ImageDraw.Draw(layer)
    shifted = (box[0] + offset[0], box[1] + offset[1], box[2] + offset[0], box[3] + offset[1])
    d.rounded_rectangle(shifted, radius=radius, fill=shadow)
    layer = layer.filter(ImageFilter.GaussianBlur(blur))
    base.alpha_composite(layer)
    d = ImageDraw.Draw(base)
    d.rounded_rectangle(box, radius=radius, fill=fill, outline=outline, width=width)


def circle_crop(img, size):
    img = img.convert("RGBA")
    img.thumbnail((size, size), Image.Resampling.LANCZOS)
    square = Image.new("RGBA", (size, size), (0, 0, 0, 0))
    square.alpha_composite(img, ((size - img.width) // 2, (size - img.height) // 2))
    mask = Image.new("L", (size, size), 0)
    ImageDraw.Draw(mask).ellipse((0, 0, size - 1, size - 1), fill=255)
    square.putalpha(mask)
    return square


def cover_crop(img, size):
    target_w, target_h = size
    img = img.convert("RGBA")
    scale = max(target_w / img.width, target_h / img.height)
    resized = img.resize((round(img.width * scale), round(img.height * scale)), Image.Resampling.LANCZOS)
    left = (resized.width - target_w) // 2
    top = (resized.height - target_h) // 2
    return resized.crop((left, top, left + target_w, top + target_h))


def vertical_gradient(size, top, bottom):
    w, h = size
    image = Image.new("RGBA", size)
    top_rgb = Image.new("RGBA", (1, 1), top).getpixel((0, 0))
    bottom_rgb = Image.new("RGBA", (1, 1), bottom).getpixel((0, 0))
    px = image.load()
    for y in range(h):
        t = y / max(h - 1, 1)
        color = tuple(round(top_rgb[i] * (1 - t) + bottom_rgb[i] * t) for i in range(4))
        for x in range(w):
            px[x, y] = color
    return image


def draw_status_bar(draw, x, y, w, dark=False):
    color = "#FFFFFF" if dark else INK
    draw.text((x + 34, y + 22), "9:41", font=font(28, True), fill=color)
    bx = x + w - 112
    draw.rounded_rectangle((bx, y + 28, bx + 48, y + 48), radius=7, outline=color, width=3)
    draw.rectangle((bx + 50, y + 35, bx + 54, y + 42), fill=color)
    for i, h in enumerate([8, 13, 18]):
        draw.rounded_rectangle((bx - 72 + i * 13, y + 48 - h, bx - 64 + i * 13, y + 48), 3, fill=color)
    draw.arc((bx - 38, y + 30, bx - 8, y + 58), 220, 320, fill=color, width=3)


def draw_nav(draw, x, y, w, h, active=0):
    labels = [("Home", "H"), ("Work", "W"), ("Blog", "B"), ("More", "M")]
    item_w = w / len(labels)
    for i, (label, glyph) in enumerate(labels):
        cx = x + item_w * i + item_w / 2
        icon_fill = BLUE if i == active else "#A2ACB8"
        draw.ellipse((cx - 21, y + 12, cx + 21, y + 54), fill=icon_fill)
        tw, _ = text_size(draw, glyph, font(20, True))
        draw.text((cx - tw / 2, y + 20), glyph, font=font(20, True), fill="#FFFFFF")
        tw, _ = text_size(draw, label, font(18, True))
        draw.text((cx - tw / 2, y + 62), label, font=font(18, True), fill=icon_fill)


def draw_chip(draw, x, y, text, fill, text_fill="#FFFFFF"):
    pad_x = 22
    tw, th = text_size(draw, text, font(22, True))
    box = (x, y, x + tw + pad_x * 2, y + 44)
    draw.rounded_rectangle(box, radius=22, fill=fill)
    draw.text((x + pad_x, y + 10), text, font=font(22, True), fill=text_fill)
    return box[2]


def phone_shell(base, box, screen_fill="#FFFFFF"):
    x, y, w, h = box
    shadowed_round_rect(base, (x, y, x + w, y + h), radius=78, fill="#0B1220", shadow=(11, 18, 32, 70), offset=(0, 22), blur=24)
    d = ImageDraw.Draw(base)
    inner = (x + 22, y + 22, x + w - 22, y + h - 22)
    d.rounded_rectangle(inner, radius=58, fill=screen_fill)
    notch_w = int(w * 0.32)
    d.rounded_rectangle((x + (w - notch_w) / 2, y + 34, x + (w + notch_w) / 2, y + 74), radius=20, fill="#05070C")
    d.rounded_rectangle((x + w * 0.45, y + 46, x + w * 0.55, y + 52), radius=4, fill="#171C26")
    d.ellipse((x + w * 0.61, y + 43, x + w * 0.64, y + 56), fill="#162A56")
    return inner


def draw_profile_screen(base, box):
    inner = phone_shell(base, box, "#F6F8FB")
    draw = ImageDraw.Draw(base)
    x1, y1, x2, y2 = inner
    w = x2 - x1
    draw_status_bar(draw, x1, y1, w)
    draw.text((x1 + 36, y1 + 105), "Nice to meet you", font=font(24), fill=MUTED)
    draw.text((x1 + 36, y1 + 142), "Tim Baz", font=font(48, True), fill=INK)
    draw.text((x1 + 36, y1 + 202), "Design Engineer", font=font(28, True), fill=BLUE)

    profile = circle_crop(Image.open(PROFILE_IMAGE), 232)
    base.alpha_composite(profile, (round(x1 + w - 286), round(y1 + 118)))
    draw.ellipse((x1 + w - 292, y1 + 112, x1 + w - 48, y1 + 356), outline="#FFFFFF", width=10)

    card_y = y1 + 400
    shadowed_round_rect(base, (x1 + 34, card_y, x2 - 34, card_y + 176), 34, "#FFFFFF")
    draw.text((x1 + 64, card_y + 34), "Android portfolio app", font=font(30, True), fill=INK)
    draw_multiline(draw, (x1 + 64, card_y + 82), "Kotlin, Jetpack Compose, navigation, Firebase, and clean project screens.", font(22), MUTED, w - 128, 6)

    stats_y = card_y + 220
    for i, (value, label, color) in enumerate([("5+", "years building apps", BLUE), ("460+", "GitHub stars", GREEN), ("2026", "client work", YELLOW)]):
        cx = x1 + 54 + i * ((w - 108) / 3)
        shadowed_round_rect(base, (cx, stats_y, cx + (w - 128) / 3, stats_y + 142), 28, "#FFFFFF", shadow=(15, 25, 50, 28), offset=(0, 10), blur=16)
        draw.text((cx + 22, stats_y + 22), value, font=font(32, True), fill=color)
        draw_multiline(draw, (cx + 22, stats_y + 70), label, font(18), MUTED, (w - 128) / 3 - 44, 3)

    about_y = stats_y + 182
    draw.text((x1 + 42, about_y), "About", font=font(32, True), fill=INK)
    for i, text in enumerate(["San Francisco Bay Area", "Android, Kotlin, Compose", "Next.js, React, TypeScript"]):
        yy = about_y + 58 + i * 76
        shadowed_round_rect(base, (x1 + 40, yy, x2 - 40, yy + 58), 24, "#FFFFFF", shadow=(15, 25, 50, 20), offset=(0, 6), blur=12)
        draw.ellipse((x1 + 62, yy + 18, x1 + 84, yy + 40), fill=[BLUE, TEAL, GREEN][i])
        draw.text((x1 + 104, yy + 16), text, font=font(22, True), fill=INK)
    draw_nav(draw, x1 + 20, y2 - 116, w - 40, 96, 0)


def draw_portfolio_screen(base, box):
    inner = phone_shell(base, box, "#FFFFFF")
    draw = ImageDraw.Draw(base)
    x1, y1, x2, y2 = inner
    w = x2 - x1
    draw_status_bar(draw, x1, y1, w)
    draw.text((x1 + 36, y1 + 112), "Portfolio", font=font(52, True), fill=INK)
    draw.text((x1 + 38, y1 + 178), "Selected Android and web projects", font=font(22), fill=MUTED)

    y = y1 + 242
    projects = [
        ("Portfolio App 2.0", "Jetpack Compose app shell", BLUE),
        ("Ponda App", "Read key ideas in text and audio", GREEN),
        ("Menu CEO", "Restaurant menu platform", YELLOW),
        ("Choijin Restaurant", "Booking and client login", TEAL),
    ]
    for i, (title, body, color) in enumerate(projects):
        shadowed_round_rect(base, (x1 + 34, y, x2 - 34, y + 150), 32, "#F8FAFD", shadow=(15, 25, 50, 24), offset=(0, 8), blur=14)
        draw.rounded_rectangle((x1 + 58, y + 28, x1 + 126, y + 96), radius=20, fill=color)
        draw.text((x1 + 77, y + 43), title[:1], font=font(34, True), fill="#FFFFFF")
        draw.text((x1 + 148, y + 30), title, font=font(26, True), fill=INK)
        draw.text((x1 + 148, y + 68), body, font=font(19), fill=MUTED)
        chip_x = draw_chip(draw, x1 + 148, y + 104, ["Kotlin", "Audio", "SaaS", "Next.js"][i], "#E8F0FE", BLUE)
        draw_chip(draw, chip_x + 8, y + 104, ["Compose", "Play", "Product", "Booking"][i], "#E8F7F4", TEAL)
        y += 178
    draw_nav(draw, x1 + 20, y2 - 116, w - 40, 96, 1)


def draw_blog_screen(base, box):
    inner = phone_shell(base, box, "#FBFCFE")
    draw = ImageDraw.Draw(base)
    x1, y1, x2, y2 = inner
    w = x2 - x1
    draw_status_bar(draw, x1, y1, w)
    draw.text((x1 + 36, y1 + 112), "Story", font=font(52, True), fill=INK)
    draw.text((x1 + 38, y1 + 178), "Background, projects, and learning notes", font=font(22), fill=MUTED)

    y = y1 + 248
    articles = [
        ("How I got started", "Java, XML, and the path into Android development."),
        ("Web development", "Next.js, React, TypeScript, and Tailwind projects."),
        ("Today", "Client work, Menu CEO, and product experiments."),
    ]
    colors = [BLUE, TEAL, GREEN]
    for i, (title, body) in enumerate(articles):
        shadowed_round_rect(base, (x1 + 34, y, x2 - 34, y + 190), 34, "#FFFFFF", shadow=(15, 25, 50, 24), offset=(0, 10), blur=16)
        draw.rounded_rectangle((x1 + 62, y + 34, x1 + 118, y + 90), radius=16, fill=colors[i])
        draw.text((x1 + 80, y + 47), str(i + 1), font=font(27, True), fill="#FFFFFF")
        draw.text((x1 + 142, y + 34), title, font=font(30, True), fill=INK)
        draw_multiline(draw, (x1 + 142, y + 78), body, font(21), MUTED, w - 210, 5)
        y += 224
    draw_nav(draw, x1 + 20, y2 - 116, w - 40, 96, 2)


def draw_contact_screen(base, box):
    inner = phone_shell(base, box, "#FFFFFF")
    draw = ImageDraw.Draw(base)
    x1, y1, x2, y2 = inner
    w = x2 - x1
    draw_status_bar(draw, x1, y1, w)
    draw.text((x1 + 36, y1 + 112), "Contact", font=font(52, True), fill=INK)
    draw.text((x1 + 38, y1 + 178), "Find Tim online or send a message", font=font(22), fill=MUTED)

    profile = circle_crop(Image.open(PROFILE_IMAGE), 190)
    base.alpha_composite(profile, (round(x1 + (w - 190) / 2), round(y1 + 244)))
    tw, _ = text_size(draw, "Tim Baz", font(34, True))
    draw.text((x1 + (w - tw) / 2, y1 + 462), "Tim Baz", font=font(34, True), fill=INK)
    tw, _ = text_size(draw, "Design Engineer", font(23, True))
    draw.text((x1 + (w - tw) / 2, y1 + 506), "Design Engineer", font=font(23, True), fill=BLUE)

    y = y1 + 586
    links = [("Email", "hiretimsf@gmail.com", BLUE), ("GitHub", "github.com/hiretimsf", INK), ("LinkedIn", "linkedin.com/in/hiretimsf", TEAL)]
    for label, value, color in links:
        shadowed_round_rect(base, (x1 + 38, y, x2 - 38, y + 108), 30, "#F8FAFD", shadow=(15, 25, 50, 22), offset=(0, 8), blur=14)
        draw.ellipse((x1 + 62, y + 24, x1 + 122, y + 84), fill=color)
        draw.text((x1 + 84, y + 39), label[:1], font=font(27, True), fill="#FFFFFF")
        draw.text((x1 + 148, y + 24), label, font=font(24, True), fill=INK)
        draw.text((x1 + 148, y + 58), value, font=font(20), fill=MUTED)
        y += 136
    draw_nav(draw, x1 + 20, y2 - 116, w - 40, 96, 3)


def draw_background(size, top="#EAF4FF", bottom="#F9FBFF"):
    image = vertical_gradient(size, top, bottom)
    draw = ImageDraw.Draw(image)
    for i in range(12):
        x = -160 + i * 140
        draw.line((x, size[1], x + 620, -60), fill=(255, 255, 255, 54), width=2)
    return image


def screenshot_canvas(title, accent, renderer, subtitle=None):
    canvas = draw_background((1080, 1920))
    draw = ImageDraw.Draw(canvas)
    draw_multiline(draw, (70, 92), title, font(76, True), INK, 940, 13, "center")
    if subtitle:
        draw_multiline(draw, (150, 285), subtitle, font(30), MUTED, 780, 8, "center")
    draw.rounded_rectangle((402, 376, 678, 386), radius=5, fill=accent)
    renderer(canvas, (200, 498, 680, 1280))
    return canvas.convert("RGB")


def create_screenshots():
    shots = [
        ("01-profile.png", "Meet Tim Baz", "Design engineer building Android and web products.", BLUE, draw_profile_screen),
        ("02-portfolio.png", "Explore the Work", "Project screens for apps, websites, and product experiments.", TEAL, draw_portfolio_screen),
        ("03-story.png", "Read the Story", "Background, learning notes, and current work in one place.", GREEN, draw_blog_screen),
        ("04-contact.png", "Connect Directly", "Email, GitHub, LinkedIn, and profile links are easy to reach.", YELLOW, draw_contact_screen),
    ]
    shot_dir = OUT / "phone-screenshots"
    shot_dir.mkdir(parents=True, exist_ok=True)
    for name, title, subtitle, accent, renderer in shots:
        screenshot_canvas(title, accent, renderer, subtitle).save(shot_dir / name, "PNG", optimize=True)


def create_feature_graphic():
    canvas = draw_background((1024, 500), "#E8F4FF", "#FFFFFF")
    draw = ImageDraw.Draw(canvas)
    profile = circle_crop(Image.open(PROFILE_IMAGE), 190)
    shadowed_round_rect(canvas, (56, 82, 318, 416), 36, "#FFFFFF", shadow=(15, 25, 50, 36), offset=(0, 12), blur=18)
    canvas.alpha_composite(profile, (92, 112))
    draw.text((102, 326), "Tim Baz", font=font(38, True), fill=INK)
    draw.text((102, 372), "Design Engineer", font=font(24, True), fill=BLUE)

    draw.text((374, 74), "HireTimSF", font=font(70, True), fill=INK)
    draw_multiline(draw, (378, 154), "A polished Android portfolio for projects, background, blog notes, and contact links.", font(28), MUTED, 420, 8)
    draw_chip(draw, 378, 284, "Kotlin", BLUE)
    draw_chip(draw, 502, 284, "Jetpack Compose", TEAL)
    draw_chip(draw, 378, 344, "Portfolio", GREEN)

    phone = Image.new("RGBA", (280, 500), (0, 0, 0, 0))
    draw_portfolio_screen(phone, (0, 0, 280, 500))
    phone = phone.resize((178, 318), Image.Resampling.LANCZOS)
    phone = phone.rotate(-7, expand=True, resample=Image.Resampling.BICUBIC)
    canvas.alpha_composite(phone, (802, 126))

    return canvas.convert("RGB")


def create_store_icon():
    icon = vertical_gradient((512, 512), "#EAF4FF", "#FFFFFF")
    draw = ImageDraw.Draw(icon)
    shadowed_round_rect(icon, (44, 44, 468, 468), 104, "#FFFFFF", shadow=(15, 25, 50, 70), offset=(0, 18), blur=22)
    draw.rounded_rectangle((72, 72, 440, 440), 88, fill="#102033")
    for i, color in enumerate([BLUE, TEAL, GREEN]):
        y = 112 + i * 72
        draw.rounded_rectangle((112, y, 400, y + 42), radius=21, fill=color)
    draw.text((126, 297), "TB", font=font(110, True), fill="#FFFFFF")
    draw.text((132, 397), "HireTimSF", font=font(30, True), fill="#B7D8FF")
    return icon.convert("RGB")


def create_preview_grid():
    shots = [Image.open(OUT / "phone-screenshots" / f"{i:02d}-{name}.png").resize((270, 480), Image.Resampling.LANCZOS) for i, name in enumerate(["profile", "portfolio", "story", "contact"], start=1)]
    grid = Image.new("RGB", (1140, 540), "#E8EEF5")
    for i, shot in enumerate(shots):
        x = 24 + i * 279
        grid.paste(shot, (x, 30))
    grid.save(OUT / "preview-grid.png", "PNG", optimize=True)


def main():
    OUT.mkdir(parents=True, exist_ok=True)
    (OUT / "icon").mkdir(parents=True, exist_ok=True)
    create_screenshots()
    create_feature_graphic().save(OUT / "feature-graphic-1024x500.png", "PNG", optimize=True)
    create_store_icon().save(OUT / "icon/app-icon-512.png", "PNG", optimize=True)
    if APP_ICON_IMAGE.exists():
        Image.open(APP_ICON_IMAGE).convert("RGB").save(OUT / "icon/current-app-icon-512.png", "PNG", optimize=True)
    create_preview_grid()
    print(f"Generated Play Store assets in {OUT}")


if __name__ == "__main__":
    main()
