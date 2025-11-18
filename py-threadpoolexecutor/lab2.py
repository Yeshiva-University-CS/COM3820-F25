import time
import random
from concurrent.futures import ThreadPoolExecutor

class Order:
    def __init__(self, order_id, order_type, amount):
        self.order_id = order_id
        self.order_type = order_type
        self.amount = amount
    
    def __repr__(self):
        return f"Order({self.order_id}, {self.order_type}, ${self.amount})"

def process_standard_order(order):
    """Process a standard order - takes 1 second"""
    time.sleep(1)
    fee = order.amount * 0.02
    return f"Order {order.order_id}: Standard processed, fee=${fee:.2f}"

def process_express_order(order):
    """Process an express order - takes 0.5 seconds"""
    time.sleep(0.5)
    fee = order.amount * 0.05
    return f"Order {order.order_id}: Express processed, fee=${fee:.2f}"

def process_international_order(order):
    """Process an international order - takes 2 seconds"""
    time.sleep(2)
    fee = order.amount * 0.08
    return f"Order {order.order_id}: International processed, fee=${fee:.2f}"

# Sample orders - mix of types
orders = [
    Order(1, "standard", 100),
    Order(2, "standard", 150),
    Order(3, "express", 200),
    Order(4, "standard", 120),
    Order(5, "international", 300),
    Order(6, "express", 180),
    Order(7, "standard", 90),
    Order(8, "international", 250),
]

# Part A: Use map() when all orders are the same type
def process_with_map(orders):
    """Process orders using map() - assumes all same type"""
    # TODO: Use map() to process all orders
    # Hint: This works best when using same function for all
    pass

# Part B: Use submit() when orders need different processing
def process_with_submit(orders):
    """Process orders using submit() - handles different types"""
    # TODO: Use submit() to process orders with appropriate function
    # 1. Loop through orders
    # 2. Check order.order_type
    # 3. Submit to appropriate function
    # 4. Collect futures and get results
    pass

if __name__ == "__main__":
    # Part A: All standard orders (use map)
    standard_orders = [o for o in orders if o.order_type == "standard"]
    print("=== Processing Standard Orders with map() ===")
    start = time.time()
    # YOUR CODE HERE: call process_with_map
    print(f"Time: {time.time() - start:.2f}s\n")
    
    # Part B: Mixed order types (use submit)
    print("=== Processing Mixed Orders with submit() ===")
    start = time.time()
    # YOUR CODE HERE: call process_with_submit
    print(f"Time: {time.time() - start:.2f}s")