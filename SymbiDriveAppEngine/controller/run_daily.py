'''
Created on Apr 28, 2015

@author: andreea
'''
from model.user import User
from model.deleted_user import DeletedUser, SocialIdentifier
import datetime
from model.pool import Pool

'''
    The results of this function will be checked by the wrapper module
    A "bad_user" will receive a push notification to inform him/her that the 
    negative rating has led to his/her exclusion from the app users
'''    
negative_words = ['abysmal', 'adverse', 'alarming', 'angry', 'annoy', 'anxious', 'apathy', 'appalling', 'atrocious', 'awful', 'bad', 'banal', 'barbed', 'belligerent', 'bemoan', 'beneath', 'boring', 'broken', 'callous', "can't", 'clumsy', 'coarse', 'cold', 'cold-hearted', 'collapse', 'confused', 'contradictory', 'contrary', 'corrosive', 'corrupt', 'crazy', 'creepy', 'criminal', 'cruel', 'cry', 'cutting', 'dead', 'decaying', 'damage', 'damaging', 'dastardly', 'deplorable', 'depressed', 'deprived', 'deformed', 'D Cont.', 'deny', 'despicable', 'detrimental', 'dirty', 'disease', 'disgusting', 'disheveled', 'dishonest', 'dishonorable', 'dismal', 'distress', "don't", 'dreadful', 'dreary', 'enraged', 'eroding', 'evil', 'fail', 'faulty', 'fear', 'feeble', 'fight', 'filthy', 'foul', 'frighten', 'frightful', 'gawky', 'ghastly', 'grave', 'greed', 'grim', 'grimace', 'gross', 'grotesque', 'gruesome', 'guilty', 'haggard', 'hard', 'hard-hearted', 'harmful', 'hate', 'hideous', 'homely', 'horrendous', 'horrible', 'hostile', 'hurt', 'hurtful', 'icky', 'ignore', 'ignorant', 'ill', 'immature', 'imperfect', 'impossible', 'inane', 'inelegant', 'infernal', 'injure', 'injurious', 'insane', 'insidious', 'insipid', 'jealous', 'junky', 'lose', 'lousy', 'lumpy', 'malicious', 'mean', 'menacing', 'messy', 'misshapen', 'missing', 'misunderstood', 'moan', 'moldy', 'monstrous', 'naive', 'nasty', 'naughty', 'negate', 'negative', 'never', 'no', 'nobody', 'nondescript', 'nonsense', 'not', 'noxious', 'objectionable', 'odious', 'offensive', 'old', 'oppressive', 'pain', 'perturb', 'pessimistic', 'petty', 'plain', 'poisonous', 'poor', 'prejudice', 'questionable', 'quirky', 'quit', 'reject', 'renege', 'repellant', 'reptilian', 'repulsive', 'repugnant', 'revenge', 'revolting', 'rocky', 'rotten', 'rude', 'ruthless', 'sad', 'savage', 'scare', 'scary', 'scream', 'severe', 'shoddy', 'shocking', 'sick', 'sickening', 'sinister', 'slimy', 'smelly', 'sobbing', 'sorry', 'spiteful', 'sticky', 'stinky', 'stormy', 'stressful', 'stuck', 'stupid', 'substandard', 'suspect', 'suspicious', 'tense', 'terrible', 'terrifying', 'threatening', 'ugly', 'undermine', 'unfair', 'unfavorable', 'unhappy', 'unhealthy', 'unjust', 'unlucky', 'unpleasant', 'upset', 'unsatisfactory', 'unsightly', 'untoward', 'unwanted', 'unwelcome', 'unwholesome', 'unwieldy', 'unwise', 'upset', 'vice', 'vicious', 'vile', 'villainous', 'vindictive', 'wary', 'weary', 'wicked', 'woeful', 'worthless', 'wound', 'yell', 'yucky', 'zero']

def check_feedback(user):
    for feedback in user.feedback:
        if any(s in feedback for s in negative_words):
            return False
    return True

def check_rating(user):
    if (user.rating is None):
        True
    elif (user.rating.number < 5):
        True
    elif (user.rating.average < 3.5):
        return False
    return True


def delete_user(user):
    deletedUser = DeletedUser(SocialIdentifier = 
                SocialIdentifier(socialID = user.socialProfile.socialID,
                                profile = user.socialProfile.profile),
                deviceID = user.deviceID)
    deletedUser.put()
    
    user.key.delete()
    
def check_users():
    users = User.query().fetch(1000)
    bad_users = []
    for user in users:
        if not check_feedback(user) or not check_rating(user):
            delete_user(user)
            bad_users.append(user)
        
    return bad_users

def delete_old_pools():
    yestarday = datetime.datetime.now() - datetime.timedelta(days=1)
    Pool.query(Pool.date <= yestarday).fetch(1000).delete_all()


    