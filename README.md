# PPS-25-scalarmonies

A simplified digital implementation of the **Harmonies** board game, developed in **Scala 3** using **ScalaFX** for the graphical user interface.

This project was developed as part of the **Programming Paradigms and Development (PPS)** course at the University of Bologna.

## Overview

Harmonies is a strategic board game in which players build landscapes by placing terrain tokens on their personal boards and complete animal habitats to score points.

The project reproduces the core gameplay mechanics of the original game in a simplified digital version, following object-oriented and functional programming principles.

## Features

- Turn-based gameplay
- Terrain token placement with rule validation
- Animal card management
- Personal and central boards
- Turn state management
- Graphical user interface built with ScalaFX
- Unit testing with ScalaTest

## Technologies

- Scala 3
- ScalaFX
- SBT
- ScalaTest
- GitHub Actions (Continuous Integration)

## Project Structure

```
src/
├── main/
│   ├── scala/
│   │   └── it/unibo/
│   │       ├── model/
│   │       ├── controller/
│   │       └── view/
│   └── resources/
└── test/
    └── scala/
```

## Build and Run

Clone the repository:

```bash
git clone <repository-url>
cd <repository-name>
```

Compile the project:

```bash
sbt compile
```

Run the application:

```bash
sbt run
```

Run the test suite:

```bash
sbt test
```

## Documentation

The project documentation is available in the `docs/` directory and includes:

- Development Process
- Requirements
- Design
- Implementation
- Testing
- Retrospective
- Process Backlog

## Authors
- [@elisayan](https://github.com/elisayan)
- [@Filoferro03](https://github.com/Filoferro03)
- [@DanAriyo](https://github.com/DanAriyo)


## License

This project is developed exclusively for educational purposes.