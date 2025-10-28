// Game Catalogue Data
const catalogue = [
  {
    id: 1,
    name: "African Savanna Puzzle",
    price: 19.99,
    category: "Puzzle",
    description:
      "Immerse yourself in the wild with this stunning puzzle featuring the African savanna and its diverse wildlife.",
    options: [
      { name: "1000 Pieces", priceModifier: 0 },
      { name: "2500 Pieces", priceModifier: 10 },
      { name: "500 Pieces", priceModifier: -5 },
    ],
  },
  {
    id: 2,
    name: "Dungeons and Dragons",
    price: 79.99,
    category: "RPG",
    description:
      "Embark on an epic adventure in a vast fantasy world filled with magic, monsters, and legendary treasures.",
    options: [
      { name: "Standard Edition", priceModifier: 0 },
      { name: "Collector's Edition", priceModifier: 20 },
      { name: "Mystic Creatures Expansion", priceModifier: -15 },
    ],
  },
  {
    id: 3,
    name: "Snakes and Ladders",
    price: 39.99,
    category: "Strategy",
    description: "Experience the thrill of high-stakes board gaming with new twists and challenges in this classic game.",
    options: [
      { name: "Base Game", priceModifier: 0 },
      { name: "2025 Reboot Edition", priceModifier: 9.99 },
    ],
  },
  {
    id: 4,
    name: "Mystery Manor",
    price: 29.99,
    category: "Strategy",
    description: "Solve intricate puzzles and uncover dark secrets in this atmospheric mystery adventure game.",
    options: [{ name: "Standard Edition", priceModifier: 0 }],
  },
  {
    id: 5,
    name: "Risk",
    price: 54.99,
    category: "Strategy",
    description:
      "Conquer the world in this classic strategy game with new maps, missions, and multiplayer modes for endless fun.",
    options: [
      { name: "Standard Edition", priceModifier: 0 },
      { name: "Enhanced Edition", priceModifier: 18 },
    ],
  },
  {
    id: 6,
    name: "30 Seconds",
    price: 34.99,
    category: "Trivia",
    description: "A fun and engaging trivia game perfect for parties, featuring a wide range of topics and difficulty levels.",
    options: [
      { name: "Standard Edition", priceModifier: 0 },
      { name: "UK Edition", priceModifier: 12 },
    ],
  },
  {
    id: 7,
    name: "Ultimate Sports Trivia",
    price: 19.99,
    category: "Trivia",
    description:
      "The ultimate sports trivia game featuring thousands of questions across all major sports and leagues.",
    options: [
      { name: "Standard Edition", priceModifier: 0 },
      { name: "All-Star Edition", priceModifier: 20 },
    ],
  },
  {
    id: 8,
    name: " Hues and Cues",
    price: 19.99,
    category: "Indie",
    description: "A charming color-themed party game that challenges your creativity and color knowledge.",
    options: [{ name: "Standard Edition", priceModifier: 0 }],
  },
  {
    id: 9,
    name: "Catan",
    // this is my favorite board game!
    price: 19.99,
    category: "Strategy",
    description:
      "Build, trade, and settle in this beloved strategy game set on the island of Catan with new expansions available.",
    options: [
      { name: "Base Game", priceModifier: 0 },
      { name: "Seafarers Expansion", priceModifier: 5 },
      { name: "Cities & Knights Expansion", priceModifier: 9 },
      { name: "Travel Edition", priceModifier: -10 },
      { name: "5-6 Player Extension", priceModifier: 7 },
    ],
  },
  {
    id: 10,
    name: "Codenames",
    price: 14.99,
    category: "Strategy",
    description:
      "A social word game where players give one-word clues to help their team identify secret agents among a grid of words.",
    options: [
      { name: "Standard Edition", priceModifier: 0 },
      { name: "Pictures Edition", priceModifier: 5 },
    ],
  },
  {
    id: 11,
    name: "Banana Grams",
    price: 14.99,
    category: "Word",
    description:
      "A fast-paced word game where players race to create interconnected words using letter tiles in a Scrabble-like fashion.",
    options: [
      { name: "Standard Edition", priceModifier: 0 },
      { name: "Travel Edition", priceModifier: -7 },
    ],
  },
  {
    id: 12,
    name: "Scrabble",
    price: 19.99,
    category: "Word",
    description:
      "The classic word game where players use letter tiles to create words on a game board for points.",
    options: [
      { name: "Standard Edition", priceModifier: 0 },
      { name: "Travel Edition", priceModifier: -10 },
    ],
  }
]

// Discount Codes
const discountCodes = {
  GAMESNOW: { type: "percentage", value: 12, description: "12% off" },
  SAVE20: { type: "percentage", value: 20, description: "20% off" },
  WELCOME: { type: "fixed", value: 5, description: "£5 off" },
  BIGDEAL: { type: "fixed", value: 15, description: "£15 off" },
  5032: {type: "fixed", value: 50.32, description: "Just Over £50 off" },
  // Added a high value discount code for testing
  // goes down to 0 as the max discount is the subtotal
}

// Application State
let order = []
let currentDiscount = null
let filteredCatalogue = [...catalogue]

// Initialize the application
function init() {
  renderCatalogue()
  setupEventListeners()
}

// Render the catalogue
function renderCatalogue() {
  const catalogueList = document.getElementById("catalogueList")
  catalogueList.innerHTML = ""

  if (filteredCatalogue.length === 0) {
    catalogueList.innerHTML = '<p class="empty-order">No games found matching your search.</p>'
    return
  }

  filteredCatalogue.forEach((item) => {
    const itemElement = document.createElement("div")
    itemElement.className = "catalogue-item"

    const optionsHTML =
      item.options.length > 1
        ? `
            <div class="item-options">
                <label for="option-${item.id}">Edition:</label>
                <select id="option-${item.id}">
                    ${item.options
                      .map(
                        (option, index) => `
                        <option value="${index}">
                          ${option.name}${option.priceModifier ? ` 
                            (${option.priceModifier > 0 ? '+' : '-'}
                            £${Math.abs(option.priceModifier).toFixed(2)})` : ''}
                        </option>
                    `,
                      )
                      .join("")}
                </select>
            </div>
        `
        : ""

    itemElement.innerHTML = `
            <div class="item-header">
                <h3 class="item-name">${item.name}</h3>
                <span class="item-category">${item.category}</span>
            </div>
            <p class="item-price">£${item.price.toFixed(2)}</p>
            <p class="item-description">${item.description}</p>
            ${optionsHTML}
            <button class="btn-primary" onclick="addToOrder(${item.id})">Add to Cart</button>
        `

    catalogueList.appendChild(itemElement)
  })
}

// Add item to order
function addToOrder(itemId) {
  const item = catalogue.find((i) => i.id === itemId)
  if (!item) return

  let selectedOption = 0
  const optionSelect = document.getElementById(`option-${itemId}`)
  if (optionSelect) {
    selectedOption = Number.parseInt(optionSelect.value)
  }

  const option = item.options[selectedOption]
  const finalPrice = item.price + option.priceModifier

  const orderItem = {
    id: Date.now(), // Create a unique ID for each order item
                    // if there are multiple people shopping at the same time, this may not be unique enough? 
                    //  for this practical it should be fine
    gameId: item.id,
    name: item.name,
    option: option.name,
    price: finalPrice,
  }
  console.log("Adding to order:", orderItem)
  order.push(orderItem)
  renderOrder()
}

// Remove item from order
function removeFromOrder(orderItemId) {
  order = order.filter((item) => item.id !== orderItemId)
  renderOrder()
  console.log("Removed item from order, remaining in order:", order)  
}

// Render the order
function renderOrder() {
  const orderList = document.getElementById("orderList")

  if (order.length === 0) {
    orderList.innerHTML = '<p class="empty-order">Your cart is empty</p>'
  } else {
    orderList.innerHTML = order
      .map(
        (item) => `
            <div class="order-item">
                <div class="order-item-header">
                    <span class="order-item-name">${item.name}</span>
                    <span class="order-item-price">£${item.price.toFixed(2)}</span>
                </div>
                <p class="order-item-option">${item.option}</p>
                <button class="btn-remove" onclick="removeFromOrder(${item.id})">Remove</button>
            </div>
        `,
      )
      .join("")
  }

  updateTotals()
  console.log("Order Rendered:", order )
}

// Update order totals
function updateTotals() {
  const subtotal = order.reduce((sum, item) => sum + item.price, 0)
  let discountAmount = 0

  if (currentDiscount) {
    if (currentDiscount.type === "percentage") {
      discountAmount = subtotal * (currentDiscount.value / 100)
    } else if (currentDiscount.type === "fixed") {
      discountAmount = Math.min(currentDiscount.value, subtotal)
    }
  }

  const total = subtotal - discountAmount
  console.log("Subtotal:", subtotal, "Discount:", discountAmount, "Total:", total)

  document.getElementById("subtotal").textContent = `£${subtotal.toFixed(2)}`
  document.getElementById("total").textContent = `£${total.toFixed(2)}`

  const discountDisplay = document.getElementById("discountDisplay")
  if (discountAmount > 0) {
    discountDisplay.style.display = "flex"
    document.getElementById("discountAmount").textContent = `-£${discountAmount.toFixed(2)}`
  } else {
    discountDisplay.style.display = "none"
  }
}

// Apply discount code
function applyDiscount() {
  const codeInput = document.getElementById("discountCode")
  const code = codeInput.value.trim().toUpperCase()

  if (code === "") {
    alert("Please enter a discount code.")
    return
  }

  if (discountCodes[code]) {
    currentDiscount = discountCodes[code]
    // alert(`Discount applied: ${currentDiscount.description}`)
    // Removed alert because the discount shows in the cart totals 
    updateTotals()
    console.log("Applied discount code:", code)
  } else {
    alert("Invalid discount code.")
    currentDiscount = null
    updateTotals()
    console.log("Failed to apply discount code:", code)
  }
}

// Search functionality
function searchCatalogue() {
  const searchTerm = document.getElementById("searchInput").value.toLowerCase()

  filteredCatalogue = catalogue.filter((item) => {
    const nameMatch = item.name.toLowerCase().includes(searchTerm)
    const categoryMatch = item.category.toLowerCase().includes(searchTerm)
    return nameMatch || categoryMatch
  })

  applySorting()
}

// Sort functionality
function sortCatalogue() {
  const sortValue = document.getElementById("sortSelect").value
  applySorting(sortValue)
}

function applySorting(sortValue) {
  if (!sortValue) {
    sortValue = document.getElementById("sortSelect").value
  }

  switch (sortValue) {
    case "price-asc":
      filteredCatalogue.sort((a, b) => a.price - b.price)
      break
    case "price-desc":
      filteredCatalogue.sort((a, b) => b.price - a.price)
      break
    case "category":
      filteredCatalogue.sort((a, b) => a.category.localeCompare(b.category))
      break
    case "default":
      filteredCatalogue.sort((a, b) => a.id - b.id)
      break
  }

  renderCatalogue()
}

// Setup event listeners
function setupEventListeners() {
  document.getElementById("applyDiscount").addEventListener("click", applyDiscount)
  document.getElementById("searchInput").addEventListener("input", searchCatalogue)
  document.getElementById("sortSelect").addEventListener("change", sortCatalogue)

  // Allow Enter key to apply discount
  document.getElementById("discountCode").addEventListener("keypress", (e) => {
    if (e.key === "Enter") {
      applyDiscount()
    }
  })
}

// Initialize when DOM is loaded
document.addEventListener("DOMContentLoaded", init)
