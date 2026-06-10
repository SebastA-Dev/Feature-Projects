# 🚀 Feature-Projects Repository

## 🌐 Project Overview

This repository is a collection of innovative software projects designed to showcase advanced technological solutions across various domains.

---

# 📦 Project 1: E-Commerce Microservices Platform

## 🎯 Project Purpose
A scalable, robust e-commerce web application built using a microservices architecture to provide high performance, flexibility, and seamless user experience.

## 🔧 Technology Stack

### Backend Microservices
- **Languages & Frameworks**
  - 🐍 Python (FastAPI)
  - ☕ Java (Micronaut)

### Infrastructure & Tools
- 🐳 **Containerization**: Docker
- 🧊 **Orchestration**: Kubernetes
- 💾 **Databases**:
  - MySQL (Relational Database)
  - Redis (Caching)

### Cloud & Security
- ☁️ **Cloud Platform**: AWS Cloud
- 🔐 **Authentication**: OAuth 2.0
- 📝 **Logging**: Comprehensive logging mechanisms

### Frontend
- ⚛️ **Web Framework**: React

## 🏗️ Architectural Considerations
- Microservices-based architecture
- Scalable and modular design
- Stateless service implementation
- Event-driven communication

---

# 📦 Project 2: Hybrid Quantum-Inspired Image Encryption (DTQW & Galois)

## 🎯 Project Purpose
A high-performance image encryption system utilizing **Discrete-Time Quantum Walks (DTQW)** and **Galois Field ($GF(2^8)$)** arithmetic. The project implements a reversible, block-based encryption pipeline that treats images as algebraic structures, ensuring high diffusion and confusion without data loss.

The project explores modern cryptographic approaches combining:
- Quantum-inspired random walkers (DTQW)
- Finite Field Arithmetic ($GF(2^8)$)
- Chaotic dynamical systems (Logistic Map 3D)
- Block-based matrix cascading

---

## 🔧 Technology Stack

### Core Technologies
- 🐍 **Python**
- 🔢 **Galois Library** ($GF(2^8)$ arithmetic)
- 📊 **NumPy** (Linear Algebra & Matrix Operations)
- 📓 **Jupyter Notebook**

### Cryptographic Components
- ⚛️ **Discrete-Time Quantum Walks (DTQW)** (Permutation layer)
- 🔐 **Matrix Cascade Encryption** ($GF(2^8)$)
- 🌀 **Logistic 3D Chaotic Map** (Key & Seed generation)
- 🔀 **Reversible Block Diffusion**

### Image Processing
- 🖼️ RGB Channel Manipulation
- 📐 Dynamic Block-Size Partitioning ($N \times N$)
- 🔀 Global Spatial Permutations

---

## 🏗️ Encryption Architecture

The system operates on a **dynamic 7-phase pipeline** that scales to any image dimension:

### Phase 0 — Cryptographic Seeding
- Generation of $X, Y, Z$ sequences from Logistic 3D Map
- Key derivation for Galois matrices and DTQW permutations
- Dynamic key scheduling based on image dimensions

### Phase 1 — RGB Separation
- Extraction of R, G, and B channels
- Pre-processing for block-based transformation

### Phase 2 — Global Spatial Permutation
- A-priori decorrelation using $\sigma$ permutations
- Break down of spatial adjacency (confusion)

### Phase 3 — Block Transformation
- Dynamic partitioning into $N \times N$ blocks
- Padding management for non-multiple resolutions
- Galois Field ($GF(2^8)$) array instantiation

### Phase 4 — Inter-Channel Mixing
- Cross-channel diffusion within $GF(2^8)$ space
- Orthogonal mixing matrices for entropy propagation

### Phase 5 — Quantum-Inspired Diffusion (DTQW)
- Application of DTQW-based permutations
- High-entropy bit shuffling using quantum walk logic
- Fully reversible bit-level reorganization

### Phase 6 — Cascaded Matrix Encryption
- Sequential multiplication over $GF(2^8)$
- Deterministic block-wise transformation
- High-complexity non-linear diffusion

### Phase 7 — Reconstruction & Output
- Block reassembly to original image geometry
- Final global permutation layer
- High-precision output generation

---

## ⚛️ Quantum-Inspired Components

### Discrete-Time Quantum Walk (DTQW)
Implemented as a deterministic permutation operator over $GF(2^8)$ blocks.

#### Core Logic
- **Operator-Based Diffusion:** Uses shift and coin operators to map input blocks to a high-entropy state.
- **Reversibility:** Operates as a unitary-inspired permutation, allowing decryption via the inverse of the shift sequence and key-reversal.
- **Dynamic Keying:** Each walk is controlled by the state of the 3D Logistic Map, ensuring high sensitivity.

---

## 📊 Evaluation & Cryptographic Metrics

The encryption pipeline is validated through:

- **NPCR & UACI:** Measuring avalanche effects in ciphered blocks.
- **Shannon Entropy:** Validation of information distribution.
- **Key Sensitivity:** Analysis of decryption failure under minimal seed variation.
- **Correlation Analysis:** Checking the removal of adjacency patterns between adjacent pixels.
- **Reversibility Testing:** Verification of bit-perfect reconstruction.

---

## 🧠 Research & Design Principles

### Current Properties
- **Strict Reversibility:** Every transformation is biyective.
- **Hybrid Complexity:** Combining algebraic (Galois) and logic (DTQW) diffusion.
- **Scalable Blocks:** Dynamic $N \times N$ block processing.
- **Secure Key-Scheduling:** Chaotic 3D seeding.

---

## 🛠️ Repository Structure

```text
Feature-Projects/
│
├── e-commerce-platform/
│   ├── backend/
│   ├── frontend/
│   └── infrastructure/
│
├── encryption-algorithm/
│   ├── notebooks/       # Implementation & Experiments
│   ├── core/            # DTQW & Galois implementations
│   ├── experiments/     # Testing different block sizes (N=4, 8, 16)
│   └── tests/           # Reversibility verification
│
└── future-projects/
