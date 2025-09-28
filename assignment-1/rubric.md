# Multithreaded Trading System - Grading Rubric

## Overview
**Total Points: 20**  
**Single Market Maker Maximum: 17/20**  
**Multiple Market Makers Required for A grade**

---

## Core Functionality (17 points)

### **TradingSystem Lifecycle Management (8 points)**
*Includes TradingThreadFactory - thread creation, coordination, and shutdown*
- **Excellent (8 pts):** Complete start/stop, proper thread creation, graceful shutdown, clean resource management
- **Good (7 pts):** Works well with minor coordination or cleanup issues
- **Satisfactory (5-6 pts):** Basic start/stop works but has lifecycle problems or poor shutdown
- **Poor (2-4 pts):** Partial functionality - threads start but shutdown problems or resource leaks
- **Failing (0-1 pts):** Cannot start system or create working threads

### **Statistics and Thread Safety (6 points)**
- **Excellent (6 pts):** Correct calculations, efficient manual synchronization, no race conditions
- **Good (5 pts):** Mostly correct with minor synchronization issues or inefficiencies
- **Satisfactory (3-4 pts):** Basic calculations work but noticeable thread safety problems or poor performance
- **Poor (1-2 pts):** Calculations sometimes work but race conditions cause inconsistent results
- **Failing (0 pts):** Incorrect calculations or major synchronization failures causing data corruption

### **OrderQueue, MarketMaker, and ExecutedOrders (3 points)**
- **Excellent (3 pts):** All components functional with proper integration
- **Good (2 pts):** Components work but with minor integration issues or missing edge cases
- **Satisfactory (1 pt):** Basic functionality but significant missing features or poor integration
- **Failing (0 pts):** One or more core components completely non-functional

---

## Multiple Market Makers Implementation (3 points)

- **Excellent (3 pts):** High throughput with optimal routing, load balancing, and coordination
- **Good (2 pts):** Functional multiple MM with decent performance but minor issues
- **Poor (1 pt):** Multiple MM implemented but poor throughput or significant coordination problems
- **Failing (0 pts):** Cannot achieve functional multiple MM architecture

---

## Deductions

### **Constraint Violations**
- **Minor (-1 to -2 pts):** Uses 1-2 prohibited utilities
- **Major (-3 to -5 pts):** Uses multiple prohibited utilities
- **Severe (-5+ pts):** Ignores constraints entirely

### **Technical Issues**
- **Modified Main.java:** -2 pts
- **Poor code quality:** -0.5 to -1 pts

---
