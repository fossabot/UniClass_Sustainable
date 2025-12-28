# Required libraries: matplotlib, networkx
# Required Interpreter: python3

import os
import re
import matplotlib.pyplot as plt
import networkx as nx

SRC_ROOT = "../src/main/java/it/unisa/uniclass"

def simple_name(path: str) -> str:
    """Restituisce il nome semplice della classe, es. UtenteService."""
    base = os.path.basename(path)
    if base.endswith(".java"):
        base = base[:-5]
    return base

# pattern molto semplice per trovare nomi di tipo (ClassName)
type_pattern = re.compile(r"\b([A-Z][A-Za-z0-9_]*)\b")

# File -> Path relativo
service_files = {}
dao_files = {}


for root, _, files in os.walk(SRC_ROOT):
    for fname in files:
        if not fname.endswith(".java"):
            continue
        path = os.path.join(root, fname)
        lower = path.lower()

        if "service" in lower:
            service_files[simple_name(path)] = path
        if any(k in lower for k in ["dao", "repository"]):
            dao_files[simple_name(path)] = path


print(f"Found {len(service_files)} services, {len(dao_files)} DAOs/Repositories")

G = nx.DiGraph()
links_nodes = {}   # service_name -> numero DAO usati

# aggiungi nodi
for s in service_files:
    G.add_node(s, type="service")
for d in dao_files:
    G.add_node(d, type="dao")

for s_name, s_path in service_files.items():
    with open(s_path, encoding="utf-8") as f:
        code = f.read()

    used_daos = set()

    # trova tutti i tipi citati nel file del service
    for m in type_pattern.finditer(code):
        type_name = m.group(1)
        if type_name in dao_files:
            used_daos.add(type_name)

    # crea archi solo verso i DAO effettivamente citati
    for d_name in used_daos:
        G.add_edge(s_name, d_name)

    links_nodes[s_name] = len(used_daos)


# === METRICA: MEDIA DAO PER SERVICE E MAX ===
avg_links = sum(links_nodes.values()) / len(links_nodes) if links_nodes else 0
max_links = max(links_nodes.values()) if links_nodes else 0
num_coupled = sum(1 for v in links_nodes.values() if v == max_links)
print(f"Average real DAO links per service: {avg_links:.2f}")
print(f"Max real DAO links for a service: {max_links}")
print(f"Number of services with max links (too coupled): {num_coupled}")

# === LAYOUT E DISEGNO ===
service_nodes = [n for n, data in G.nodes(data=True) if data["type"] == "service"]
dao_nodes      = [n for n, data in G.nodes(data=True) if data["type"] == "dao"]


pos = {}
n_services = len(service_nodes)
n_daos = len(dao_nodes)

def normalized_x(i, n):
    if n == 1:
        return 0.5
    return 0.1 + 0.8 * (i / (n - 1))

for i, n in enumerate(service_nodes):
    pos[n] = (normalized_x(i, n_services), 0.8)   # services in alto
for i, n in enumerate(dao_nodes):
    pos[n] = (normalized_x(i, n_daos), 0.2)       # dao in basso


node_colors = ["red" if G.nodes[n]["type"] == "service" else "blue" for n in G.nodes]

plt.figure(figsize=(14, 8))

nx.draw(
    G, pos,
    node_color=node_colors,
    with_labels=False,
    node_size=2200,
    edge_color="gray",
    arrows=True,
    arrowsize=10,
)

# label semplici vicino ai nodi
for n, (x, y) in pos.items():
    plt.text(
        x, y + (0.06 if G.nodes[n]["type"] == "service" else -0.08),
        n,
        fontsize=7,
        rotation=45,
        ha="right" if G.nodes[n]["type"] == "service" else "left",
        va="bottom" if G.nodes[n]["type"] == "service" else "top",
    )


plt.tight_layout()
os.makedirs("output", exist_ok=True)
plt.savefig("output/service-dao-analysis.png", dpi=300, bbox_inches="tight")
plt.show()