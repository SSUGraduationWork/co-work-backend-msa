const mongoose = require("mongoose");

const chatSchema = new mongoose.Schema({
	teamId: {
		type: Number,
		required: true,
	},
    userId: {
		type: Number,
		required: true,
	},
	content: {
		type: String,
		required: true,
	},
	createdAt: {
		type: Date,
		default: Date.now,
		required: true,
	},
});

const Chat = mongoose.model("Chat", chatSchema);

module.exports = Chat;