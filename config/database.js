const mongoose = require("mongoose");

mongoose.connect(process.env.DB_URL, {
    useNewUrlParser: true,
    useUnifiedTopology: true,
    dbName: 'Cowork', //dbName으로 설정하면 된다.
});

const db = mongoose.connection;

const handleOpen = () => console.log("✅ Connected to DB");
const handleError = (error) => console.log("❌ DB Error");

db.on("error", handleError);
db.once("open", handleOpen);


