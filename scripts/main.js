"use strict";

function isWhite(ch) {
    return ch == ' ' || ch == '\r' || ch == '\n' || ch = '\t';
}

function StringStream(str) {
    this._content = str;
    this._ptr = 0;
    this._tmpPtr = 0;
}

/**
 * if the current char if not white, then move forward,
 * until the ptr goes to the next word after some spaces
 */
StringStream.prototype.goToNextWord = function () {
    while (!isWhite(this.get(this._ptr))) {
        ++this._ptr;
    }
    eatWhite();
}

StringStream.prototype.tail = function () {
    return this._content.slice(this._ptr);
}

StringStream.prototype.match = function(regexp) {
    if (regexp instanceof RegExp) {
        var tail = this.tail();
        return regexp.test(tail);
    } else if (typeof regexp == "string") {
        var str = regexp;
        for (var i = 0; i < str.length; ++i) {
            if (str.charAt(i) != this.get(this._ptr + i)) {
                return false;
            }
        }
        return true;
    }
    throw new Error("<StringStream>match: type error");
}

StringStream.prototype.swallow = function(regexp) {
    throw new Error("<StringStream>swallow: not implemented");
}

StringStream.prototype.eatWhite = function() {
    var cnt = 0;
    var ch = this.get(this._ptr);
    while (isWhite(ch)) {
        ++cnt;
        ch = this.get(this._ptr + cnt);
    }
    return cnt;
}

StringStream.prototype.eatSpace = function() {
    var cnt = 0;
    var ch = this.get(this._ptr);
    while (ch == ' ') {
        ++cnt;
        ch = this.get(this._ptr + cnt);
    }
    return cnt;
}

StringStream.prototype.get = function () {
    var ptr = this._ptr;
    if (arguments.length > 0) {
        ptr = arguments[0];
    }
    if (ptr >= this._content.length) return '\0';
    return this._content.charAt(ptr);
}

StringStream.prototype.popString = function () {
    var result = this._content.slice(this._tmpPtr, this._ptr);
    this._ptr = this._tmpPtr;
    return result;
}

function TmLangSpec(jsonObj) {
    this._jsonObj = jsonObj;
    this._handleJsonObj();
}

TmLangSpec.prototype._handleJsonObj = function() {
    var jsonObj = this._jsonObj;

    this._fileTypes = jsonObj['fileTypes'];
    this._name = jsonObj['name'];
    this._repository = jsonObj['repository'];
    this._patterns = jsonObj['patterns'];

    for (var _keyName in this._repository) {
        var _value = this._repository[_keyName];
        this.compileRegex(_value);
    }

    this._patterns.forEach(function (pat) {
        this.compileRegex(pat);
    });
}

var _nameToCompile = ['begin', 'end', 'match'];
TmLangSpec.prototype.compileRegex = function(obj) {
    _nameToCompile.forEach(function (name) {
        if (name in obj) {
            obj[name] = new RegExp(obj[name]);
        }
    });

    if ('patterns' in obj) {
        obj['patterns'].forEach(function (pat) {
            this.compileRegex(pat);
        }.bind(this));
    }
}

TmLangSpec.prototype.getName = function() {
    return this._name
}

TmLangSpec.prototype.getFileTypes = function() {
    return this._fileTypes;
}

TmLangSpec.prototype.getRepository = function(name) {
    return this._repository[name];
}

TmLangSpec.prototype.getPatterns = function () {
    return this._patterns;
}

TmLangSpec.prototype.isEnd = function () {
    return this.ptr >= this._content.length;
}

function TmLangRegistry() {
    this._map = {};
}

TmLangRegistry.prototype.register = function (name, obj) {
    if (name in this._map) {
        var value = this._map[name];
        value.push(obj);
    } else {
        this._map[name] = [obj];
    }
}

TmLangRegistry.prototype.getTmLangSpec= function (name) {
    var _list = this._map[name];
    if (_list) {
        return _list[0];
    } else {
        //TODO: return default spec
    }
}

function MainManager() {
    this._map = {};
    this._registry = new TmLangRegistry();
}

MainManager.prototype.addTmLanguageText = function(str) {
    var obj = JSON.parse(str);
    if (obj['name']) {
        this._map['name'] = obj;

        var fileTypes = obj['fileTypes'];
        if (Array.isArray(fileTypes)) {
            fileTypes.forEach(function (ft) {
                this._registry.register(ft, new TmLangSpec(obj));
            });
        }
    }
}

MainManager.prototype.tokenize = function (filename, str) {
    var tokens = [];
    var lastIndex = filename.lastIndexOf('.');
    if (lastIndex < -1) {
        // use default tokenizer;
    }
    var ft = filename.splice(lastIndex);
    var spec = this._registry.getTmLangSpec(ft);

    var patterns = spec.getPatterns();

    var stream = new StringStream(str);
    while (stream.isEnd()) {
        for (var i = 0; i < patterns.length; ++i) {
            var pattern = patterns[i];
            if ('begin' in pattern) {
            }
        }
    }
}

MainManager.prototype.handlePatterns = function (stream, patterns, repos) {
    var result = false;
    for (var i=0; i < patterns; ++i) {
        result = this.handlePattern(stream, patterns[i]);
        if (result) {
            break;
        }
    }
    return result;
}

MainManager.prototype.handlePattern = function (stream, pattern, repos) {
    if ('include' in pattern) {
        var repo = repos[pattern['include'].slice(1)];
        return this.handlePattern(stream, repo, repos);
    } else if ('begin' in pattern) {
        var ptn = pattern['begin'];
        if (stream.swallow(ptn)) {

            if ('end' in pattern) {

            } else {
                return true;
            }

        } else {
            return false;
        }
    }
}
