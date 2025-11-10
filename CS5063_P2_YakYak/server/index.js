// index.js
// YakYak API — single-file Express app with CORS
// Endpoints:
//   GET    /yaks
//   GET    /yaks/:id
//   POST   /yaks
//   POST   /yaks/:id/like
//   POST   /yaks/:id/reply
//   DELETE /yaks/:id
//
// Pagination: limit/offset with limit <= 20, returns links.self/next/prev
// Error format: { "error": "message" }

const express = require("express");
const cors = require("cors");

class Yak {
  constructor(id, text, created_at, author_name, likes, reply_to) {
    this.id = id;
    this.text = text;
    this.created_at = created_at;
    this.author_name = author_name;
    this.likes = likes;
    this.reply_to = reply_to;
  }
}

class YaksModel {
  constructor() {
    this.yaks = [];
    this.nextId = 1;
  }

  findYak(id) {
    return this.yaks.find((yak) => yak.id === id);
  }

  validateYakData(text, author_name, reply_to = null) {
    const errors = [];

    if (typeof text !== "string" || text.length === 0) {
      errors.push("text must be a non-empty string");
    } else if (text.length > 280) {
      errors.push("text must not exceed 280 characters");
    }

    if (typeof author_name !== "string" || author_name.length === 0) {
      errors.push("author_name must be a non-empty string");
    } else if (author_name.length > 64) {
      errors.push("author_name must not exceed 64 characters");
    }

    if (
      reply_to !== null &&
      (!Number.isInteger(reply_to) || !this.findYak(reply_to))
    ) {
      errors.push("reply_to must be null or a valid Yak ID");
    }

    return errors;
  }

  addYak(text, author_name, reply_to = null) {
    const errors = this.validateYakData(text, author_name, reply_to);
    if (errors.length > 0) {
      throw new Error(errors.join("; "));
    }

    const newYak = new Yak(
      this.nextId++,
      text,
      new Date().toISOString(),
      author_name,
      0,
      reply_to
    );
    this.yaks.push(newYak);
    return newYak;
  }

  getYaks(limit = 10, offset = 0, reply_to = null, search = null) {
    let filtered = [...this.yaks];

    // filter by reply_to
    if (reply_to !== null) {
      filtered = filtered.filter((yak) => yak.reply_to === reply_to);
    }

    // filter by search (case-insensitive)
    if (search) {
      const searchLower = search.toLowerCase();
      filtered = filtered.filter(
        (yak) =>
          yak.text.toLowerCase().includes(searchLower) ||
          yak.author_name.toLowerCase().includes(searchLower)
      );
    }

    // sort by newest first (assuming higher ID = newer)
    filtered.sort((a, b) => b.id - a.id);

    const total = filtered.length;
    const items = filtered.slice(offset, offset + limit);

    return { items, total };
  }

  likeYak(id) {
    const yak = this.findYak(id);
    if (!yak) {
      return null;
    }
    yak.likes++;
    return yak;
  }

  deleteYak(id) {
    const index = this.yaks.findIndex((yak) => yak.id === id);
    if (index === -1) {
      return false;
    }
    this.yaks.splice(index, 1);
    return true;
  }
}

const app = express();
app.use(express.json());
app.use(cors());

const model = new YaksModel();

// GET /yaks
app.get("/yaks", (req, res) => {
  const limit = parseInt(req.query.limit) || 10;
  const offset = parseInt(req.query.offset) || 0;
  const reply_to = req.query.reply_to ? parseInt(req.query.reply_to) : null;
  const search = req.query.search || null;

  if (limit > 20) {
    return res
      .status(400)
      .json({ error: "The 'limit' parameter must be 20 or below." });
  }

  if (offset < 0) {
    return res
      .status(400)
      .json({ error: "The 'offset' parameter must be 0 or greater." });
  }

  const { items, total } = model.getYaks(limit, offset, reply_to, search);

  const queryParams = new URLSearchParams();
  if (reply_to !== null) queryParams.set("reply_to", reply_to);
  if (search) queryParams.set("search", search);

  const baseQuery = queryParams.toString() ? `&${queryParams.toString()}` : "";

  const response = {
    items,
    limit,
    offset,
    total,
    links: {
      self: `/yaks?limit=${limit}&offset=${offset}${baseQuery}`,
      next:
        offset + limit < total
          ? `/yaks?limit=${limit}&offset=${offset + limit}${baseQuery}`
          : null,
      prev:
        offset > 0
          ? `/yaks?limit=${limit}&offset=${Math.max(
              0,
              offset - limit
            )}${baseQuery}`
          : null,
    },
  };

  res.json(response);
});

// GET /yaks/:id
app.get("/yaks/:id", (req, res) => {
  const id = parseInt(req.params.id);
  const yak = model.findYak(id);

  if (!yak) {
    return res.status(404).json({ error: "Yak not found" });
  }

  res.json(yak);
});

// POST /yaks
app.post("/yaks", (req, res) => {
  const { text, author_name, reply_to = null } = req.body;

  try {
    const newYak = model.addYak(text, author_name, reply_to);
    res.status(201).json(newYak);
  } catch (error) {
    res.status(400).json({ error: error.message });
  }
});

// POST /yaks/:id/like
app.post("/yaks/:id/like", (req, res) => {
  const id = parseInt(req.params.id);
  const yak = model.likeYak(id);

  if (!yak) {
    return res.status(404).json({ error: "Yak not found" });
  }

  res.json(yak);
});

// POST /yaks/:id/reply
app.post("/yaks/:id/reply", (req, res) => {
  const id = parseInt(req.params.id);
  const { text, author_name } = req.body;

  if (!model.findYak(id)) {
    return res.status(404).json({ error: "Yak not found" });
  }

  try {
    const newYak = model.addYak(text, author_name, id);
    res.status(201).json(newYak);
  } catch (error) {
    res.status(400).json({ error: error.message });
  }
});

// DELETE /yaks/:id
app.delete("/yaks/:id", (req, res) => {
  const id = parseInt(req.params.id);
  const deleted = model.deleteYak(id);

  if (!deleted) {
    return res.status(404).json({ error: "Yak not found" });
  }

  res.status(204).send();
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`YakYak API listening on port ${PORT}`);
});
