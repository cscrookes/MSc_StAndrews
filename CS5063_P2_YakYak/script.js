const API_URL = "http://localhost:3000/yaks";
let currentOffset = 0;
const limit = 10;
let currentSearch = null;

// Fetch and display Yaks
async function fetchYaks(offset = 0, search = null) {
  let url = `${API_URL}?limit=${limit}&offset=${offset}`;
  if (search) url += `&search=${encodeURIComponent(search)}`;
  const res = await fetch(url);
  const data = await res.json();

  currentOffset = offset;
  currentSearch = search;

  const timeline = document.getElementById("timeline");
  timeline.innerHTML = "";
  data.items.forEach(yak => {
    const yakDiv = document.createElement("div");
    yakDiv.id = `yak-${yak.id}`;
    yakDiv.innerHTML = `
      <strong>${yak.author_name}</strong> (${new Date(yak.created_at).toLocaleString()}):
      <p>${yak.text}</p>
      <p>Likes: <span class="like-count">${yak.likes}</span> 
        <button onclick="likeYak(${yak.id})">Like</button>
        <button onclick="replyYak(${yak.id})">Reply</button>
        <button onclick="deleteYak(${yak.id})">Delete</button>
      </p>
      ${yak.reply_to ? `<small>Reply to Yak #${yak.reply_to}</small>` : ""}
      <hr>
    `;
    timeline.appendChild(yakDiv);
  });

  // Handle pagination buttons
  document.getElementById("prevPage").disabled = !data.links.prev;
  document.getElementById("nextPage").disabled = !data.links.next;
}

// Post a new Yak
document.getElementById("postYak").addEventListener("click", async () => {
  const text = document.getElementById("yakText").value.trim();
  const author_name = document.getElementById("authorName").value.trim();
  if (!text || !author_name) return alert("Author and text required");

  const res = await fetch(API_URL, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ text, author_name })
  });

  if (res.ok) {
    document.getElementById("yakText").value = "";
    fetchYaks(currentOffset, currentSearch);
  } else {
    const err = await res.json();
    alert(err.error);
  }
});

// Like a Yak
async function likeYak(id) {
  const res = await fetch(`${API_URL}/${id}/like`, { method: "POST" });
  if (res.ok) {
    const yak = await res.json();
    document.querySelector(`#yak-${id} .like-count`).textContent = yak.likes;
  } else {
    alert("Yak not found");
  }
}

// Reply to a Yak
async function replyYak(id) {
  const author_name = prompt("Your name:");
  if (!author_name) return;
  const text = prompt("Your reply:");
  if (!text) return;

  const res = await fetch(`${API_URL}/${id}/reply`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ text, author_name })
  });

  if (res.ok) fetchYaks(currentOffset, currentSearch);
  else {
    const err = await res.json();
    alert(err.error);
  }
}

// Delete a Yak
async function deleteYak(id) {
  if (!confirm("Are you sure you want to delete this Yak?")) return;
  const res = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
  if (res.ok) {
    document.getElementById(`yak-${id}`).remove();
  } else {
    alert("Yak not found");
  }
}

// Pagination
document.getElementById("prevPage").addEventListener("click", () => {
  if (currentOffset - limit >= 0) fetchYaks(currentOffset - limit, currentSearch);
});
document.getElementById("nextPage").addEventListener("click", () => {
  fetchYaks(currentOffset + limit, currentSearch);
});

// Search
document.getElementById("searchButton").addEventListener("click", () => {
  const term = document.getElementById("searchTerm").value.trim();
  fetchYaks(0, term || null);
});

// Polling for new Yaks every 10 seconds
setInterval(() => fetchYaks(currentOffset, currentSearch), 10000);

// Initial load
fetchYaks();
