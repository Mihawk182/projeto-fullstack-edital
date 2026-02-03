module.exports = {
  content: ["./index.html", "./src/**/*.{ts,tsx}"],
  theme: {
    extend: {
      colors: {
        ink: "#111827",
        fog: "#f5f7fb",
        ember: "#ff6a3d",
        lake: "#0ea5e9"
      },
      boxShadow: {
        soft: "0 8px 30px rgba(17, 24, 39, 0.12)"
      }
    }
  },
  plugins: []
};
