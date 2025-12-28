from flask import Flask, request, jsonify
app = Flask(__name__)

@app.route('/explain-knn', methods=['POST'])
def explain_knn():
    data = request.get_json()
    k = data['k']
    blue = data['blue']
    red = data['red']
    prediction = data['prediction']
    
    explanation = f"k-NN (k={k}) predicted {prediction}: {blue} blue vs {red} red neighbors (majority vote)"
    
    return jsonify({
        "success": True,
        "explanation": explanation
    })

@app.route('/', methods=['GET'])
def home():
    return jsonify({"status": "Backend OK!"})

if __name__ == '__main__':
    print("🚀 Backend ready! http://localhost:5000")
    app.run(debug=True, port=5000)
