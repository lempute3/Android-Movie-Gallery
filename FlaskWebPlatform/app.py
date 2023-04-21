from flask import Flask, get_flashed_messages, request, flash
from flask import render_template
from firebase_admin import credentials, initialize_app, db
from datetime import datetime
import os

# Initialize Flask app
app = Flask(__name__)
home_index = 'entrance.html'
watchlist_index = 'watchlist.html'
app.secret_key = 'FhVhA5yVQY'

# Initialize Firebase app with credentials
cwd = os.path.dirname(os.path.abspath(__file__))
cred = credentials.Certificate(os.path.join(cwd, 'config', '...'))
firebase_app = initialize_app(cred, {
    'databaseURL': '...',
})

# Get a references to the temp users collection
users_ref = db.reference('TempUsers')
watchlist_ref = db.reference('Watchlist')

@app.route('/')
def index():
    return render_template(home_index)

@app.route('/home')
def home():
    return render_template(home_index)

@app.route('/submit_form', methods=['POST'])
def submit_form():
    if request.method == 'POST':
        
        access_key = request.form.get('access_token')
        if not access_key: 
            flash('Access key is required!')
            return render_template(home_index, messages=get_flashed_messages())
        
        user_id = get_uid(access_key)
        if not user_id: 
            flash('Invalid access key!')
            return render_template(home_index, messages=get_flashed_messages())
        
        if not check_expiration(user_id):
            flash('Access key expired!')
            return render_template(home_index, messages=get_flashed_messages())
        
        user_watchlist = get_watchlist(user_id)
        if not user_watchlist: 
            flash('Watch list is empty!')
            return render_template(home_index, messages=get_flashed_messages())
        
        return render_template(watchlist_index, watchlist=user_watchlist)
     
    return render_template(home_index, messages=get_flashed_messages())


def get_uid(access_key):      
    users = users_ref.get()
    id = ""
    for user_id, user_data in users.items():
        if 'access_key' in user_data and user_data['access_key'] == access_key:
            id = user_id
    return id

def get_watchlist(user_id):
    return watchlist_ref.child(user_id).get()

def check_expiration(user_id):
    current_datetime = datetime.now()
    timestamp = users_ref.child(user_id).child('created_at').get()
    dt_object = datetime.fromtimestamp(timestamp / 1000)
    
    # Check if 1 minute has passed since the creation time
    if (current_datetime - dt_object).total_seconds() > 60:
        return False
    
    return True
    
    

if __name__ == "__main__":
    app.run()