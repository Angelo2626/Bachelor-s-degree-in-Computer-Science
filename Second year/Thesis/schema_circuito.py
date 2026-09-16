"""Disegna lo schema del circuito VQC di riferimento.

La figura viene ridisegnata da zero per evitare di riprodurre l'immagine
pubblicata nell'articolo di riferimento.
"""

import matplotlib.pyplot as plt
from matplotlib.patches import Rectangle, FancyBboxPatch

FIG_PATH = "../figure/schema_circuito.png"

GATE_W, GATE_H = 0.62, 0.5
COL = {"enc": "#4472c4", "ans": "#c00000", "mes": "#404040"}


def gate(ax, x, y, label, color):
    ax.add_patch(FancyBboxPatch((x - GATE_W / 2, y - GATE_H / 2), GATE_W, GATE_H,
                                boxstyle="round,pad=0.02", linewidth=1.4,
                                edgecolor=color, facecolor="white", zorder=3))
    ax.text(x, y, label, ha="center", va="center", fontsize=10.5, zorder=4)


def main():
    fig, ax = plt.subplots(figsize=(9.2, 3.1))
    y0, y1 = 1.0, 0.0
    xs = [1.3, 2.4, 3.5, 4.6, 5.7]

    for y in (y0, y1):
        ax.plot([0.55, 6.5], [y, y], color="black", linewidth=1.0, zorder=1)

    ax.text(0.32, y0, r"$q_0:|0\rangle$", ha="right", va="center", fontsize=11)
    ax.text(0.32, y1, r"$q_1:|0\rangle$", ha="right", va="center", fontsize=11)

    gate(ax, xs[0], y0, r"$R_x[x_0]$", COL["enc"])
    gate(ax, xs[0], y1, r"$R_x[x_1]$", COL["enc"])
    gate(ax, xs[1], y0, r"$R_y^{w_0}$", COL["ans"])
    gate(ax, xs[1], y1, r"$R_y^{w_1}$", COL["ans"])

    ax.plot([xs[2], xs[2]], [y1, y0], color="black", linewidth=1.3, zorder=2)
    ax.plot([xs[2]], [y0], marker="o", markersize=8, color="black", zorder=4)
    circ = plt.Circle((xs[2], y1), 0.155, facecolor="white", edgecolor="black",
                      linewidth=1.3, zorder=4)
    ax.add_patch(circ)
    ax.plot([xs[2] - 0.155, xs[2] + 0.155], [y1, y1], color="black", linewidth=1.3, zorder=5)
    ax.plot([xs[2], xs[2]], [y1 - 0.155, y1 + 0.155], color="black", linewidth=1.3, zorder=5)

    gate(ax, xs[3], y0, r"$R_y^{w_2}$", COL["ans"])
    gate(ax, xs[3], y1, r"$R_y^{w_3}$", COL["ans"])

    for y in (y0, y1):
        ax.add_patch(Rectangle((xs[4] - GATE_W / 2, y - GATE_H / 2), GATE_W, GATE_H,
                               linewidth=1.4, edgecolor=COL["mes"], facecolor="white", zorder=3))
        arc = plt.matplotlib.patches.Arc((xs[4], y - 0.11), 0.36, 0.30, theta1=0, theta2=180,
                                         linewidth=1.2, color=COL["mes"], zorder=4)
        ax.add_patch(arc)
        ax.annotate("", xy=(xs[4] + 0.13, y + 0.11), xytext=(xs[4] - 0.02, y - 0.11),
                    arrowprops=dict(arrowstyle="->", linewidth=1.2, color=COL["mes"]), zorder=4)

    boxes = [
        (0.90, 0.80, "Encoding", COL["enc"]),
        (1.95, 3.05, "Ansatz variazionale", COL["ans"]),
        (5.28, 0.84, "Misurazione", COL["mes"]),
    ]
    for x_start, width, label, color in boxes:
        ax.add_patch(Rectangle((x_start, -0.45), width, 1.9, linewidth=1.2,
                               edgecolor=color, facecolor="none", linestyle="--", zorder=0))
        ax.text(x_start + width / 2, -0.72, label, ha="center", va="center",
                fontsize=10.5, color=color)

    ax.text(xs[4] + 0.75, (y0 + y1) / 2, r"classe $= \mathbb{1}[\,\hat{P}(q_0=1) > 1/2\,]$",
            ha="left", va="center", fontsize=9.5)

    ax.set_xlim(-0.35, 8.6)
    ax.set_ylim(-1.0, 1.7)
    ax.axis("off")
    plt.tight_layout()
    plt.savefig(FIG_PATH, dpi=220, bbox_inches="tight")
    print(f"Figura salvata in {FIG_PATH}")


if __name__ == "__main__":
    main()
