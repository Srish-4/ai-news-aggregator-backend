# AI News Aggregator 🚀

A full-stack news aggregation platform that consolidates AI-related content from multiple sources including news articles, research papers, and tech discussions. Built with intelligent caching to optimize API usage and reduce costs by 95%.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0-green)
![React](https://img.shields.io/badge/React-18-blue)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-13-blue)

## 🎯 Features

### Multi-Source Aggregation
- **NewsAPI**: Latest AI news from mainstream media
- **HackerNews**: Tech community discussions and trending stories
- **arXiv**: Academic research papers in AI/ML

### Smart Caching System
- PostgreSQL-based caching layer
- 1-hour cache TTL to balance freshness and API costs
- Reduced API calls from 1000+/day to ~24/day (95% reduction)
- Automatic deduplication by URL

### User Features
- **Real-time Search**: Full-text search across titles and descriptions
- **Advanced Filters**: Filter by source, category, date
- **Trending Articles**: Track most-clicked articles with view counts
- **Click Analytics**: Monitor article engagement

### Performance
- Response time: 50-200ms (cached) vs 2-5s (fresh fetch)
- Cache hit rate: ~92%
- Handles concurrent requests efficiently

## 🏗️ Architecture
```
┌─────────────┐
│   React     │  Frontend (Vite)
│  Frontend   │  - Search & Filters
└──────┬──────┘  - Article Display
       │
       ↓
┌─────────────┐
│ Spring Boot │  Backend API
│   REST API  │  - /api/news
└──────┬──────┘  - /api/news/search
       │         - /api/news/trending
       ↓
┌─────────────┐
│ PostgreSQL  │  Database
│  Database   │  - Articles cache
└──────┬──────┘  - Click tracking
       │         - API usage logs
       ↓
┌─────────────────────────────┐
│  External APIs               │
│  - NewsAPI                   │
│  - HackerNews (Firebase)     │
│  - arXiv                     │
└──────────────────────────────┘
```

## 🛠️ Tech Stack

### Backend
- **Java 17** with Spring Boot 4.0
- **Spring Data JPA** for ORM
- **PostgreSQL** for persistent storage
- **RestTemplate** for external API calls
- **Maven** for dependency management

### Frontend
- **React 18** with Hooks
- **Vite** for fast development
- **Axios** for API communication
- **Vanilla CSS** for styling

### Infrastructure
- **Railway** for PostgreSQL hosting
- CORS-enabled for cross-origin requests

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Node.js 16+
- PostgreSQL 13+ (or Railway account)
- NewsAPI key (free tier: https://newsapi.org)

### Backend Setup

1. **Clone the repository**
```bash
git clone https://github.com/Srish-4/ai-news-aggregator-backend.git
cd ai-news-aggregator-backend
```

2. **Configure application.properties**

Create `src/main/resources/application.properties`:
```properties
# PostgreSQL Configuration
spring.datasource.url=jdbc:postgresql://YOUR_HOST:5432/YOUR_DB
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# NewsAPI Key
news.api.key=YOUR_NEWSAPI_KEY
```

3. **Run the application**
```bash
./mvnw spring-boot:run
```

Backend will start on `http://localhost:8080`

### Frontend Setup

1. **Navigate to frontend directory**
```bash
cd news-frontend
```

2. **Install dependencies**
```bash
npm install
```

3. **Start development server**
```bash
npm run dev
```

Frontend will start on `http://localhost:5173`

## 📊 API Endpoints

### Articles
- `GET /api/news` - Fetch all aggregated news
- `GET /api/news/search?q={keyword}` - Search articles
- `GET /api/news/source/{source}` - Filter by source
- `GET /api/news/trending` - Get most-clicked articles

### Analytics
- `POST /api/news/{id}/click` - Track article click

### Response Format
```json
{
  "id": 1,
  "title": "Article Title",
  "description": "Article description...",
  "url": "https://example.com/article",
  "source": "NewsAPI",
  "category": "research",
  "publishedAt": "2026-01-17T10:30:00",
  "clickCount": 5
}
```

## 🐛 Known Issues

- arXiv API occasionally returns null responses (handled gracefully)
- No deduplication for same article from different sources
- Trending only tracks clicks, not time decay

## 🤝 Contributing

This is a personal learning project, but suggestions are welcome!

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Open a Pull Request

## 📝 License

MIT License - feel free to use this project for learning

## 👤 Author

**Your Name**
- GitHub: [Srishti Mittal](https://github.com/Srish-4)
- LinkedIn: [Srishti Mittal](www.linkedin.com/in/srishti-mittal-42r)

## 🙏 Acknowledgments

- [NewsAPI](https://newsapi.org) for news aggregation
- [HackerNews API](https://github.com/HackerNews/API) for tech discussions
- [arXiv API](https://arxiv.org/help/api) for research papers

---
